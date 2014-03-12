package com.liferay.portlet.blogs.trackback;

import com.google.common.base.Function;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.util.LinkbackConsumerUtil;

public class TrackbacksImpl implements Trackbacks {

	public TrackbacksImpl() {
		_comments = new TrackbackCommentsImpl();
	}

	@Override
	public void addTrackback(
		BlogsEntry entry, ThemeDisplay themeDisplay, String excerpt, String url,
		String blogName, String title,
		Function<String, ServiceContext> serviceContextFunction)
	throws PortalException, SystemException {

		long userId = UserLocalServiceUtil.getDefaultUserId(
			themeDisplay.getCompanyId());
		long groupId = entry.getGroupId();
		String className = BlogsEntry.class.getName();
		long classPK = entry.getEntryId();

		String body =
			"[...] " + excerpt + " [...] [url=" + url + "]" +
				themeDisplay.translate("read-more") + "[/url]";

		String entryURL =
			PortalUtil.getLayoutFullURL(themeDisplay) +
				Portal.FRIENDLY_URL_SEPARATOR + "blogs/" +
				entry.getUrlTitle();

		long messageId =
			_comments.addTrackbackComment(
				userId, groupId, className, classPK, blogName, title, body,
				serviceContextFunction);

		LinkbackConsumerUtil.addNewTrackback(messageId, url, entryURL);
	}

	public TrackbacksImpl(TrackbackComments comments) {
		_comments = comments;
	}

	private final TrackbackComments _comments;

}