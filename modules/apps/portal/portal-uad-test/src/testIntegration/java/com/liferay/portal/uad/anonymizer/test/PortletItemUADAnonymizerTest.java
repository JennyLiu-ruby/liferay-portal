/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.uad.anonymizer.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;

import com.liferay.portal.kernel.model.PortletItem;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.PortletItemLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.uad.test.PortletItemUADTestHelper;

import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.test.util.BaseUADAnonymizerTestCase;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;

import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@RunWith(Arquillian.class)
public class PortletItemUADAnonymizerTest extends BaseUADAnonymizerTestCase<PortletItem> {
	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new LiferayIntegrationTestRule();

	@After
	public void tearDown() throws Exception {
		_portletItemUADTestHelper.cleanUpDependencies(_portletItems);
	}

	@Override
	protected PortletItem addBaseModel(long userId) throws Exception {
		return addBaseModel(userId, true);
	}

	@Override
	protected PortletItem addBaseModel(long userId, boolean deleteAfterTestRun)
		throws Exception {
		PortletItem portletItem = _portletItemUADTestHelper.addPortletItem(userId);

		if (deleteAfterTestRun) {
			_portletItems.add(portletItem);
		}

		return portletItem;
	}

	@Override
	protected void deleteBaseModels(List<PortletItem> baseModels)
		throws Exception {
		_portletItemUADTestHelper.cleanUpDependencies(baseModels);
	}

	@Override
	protected UADAnonymizer getUADAnonymizer() {
		return _uadAnonymizer;
	}

	@Override
	protected boolean isBaseModelAutoAnonymized(long baseModelPK, User user)
		throws Exception {
		PortletItem portletItem = _portletItemLocalService.getPortletItem(baseModelPK);

		String userName = portletItem.getUserName();

		if ((portletItem.getUserId() != user.getUserId()) &&
				!userName.equals(user.getFullName())) {
			return true;
		}

		return false;
	}

	@Override
	protected boolean isBaseModelDeleted(long baseModelPK) {
		if (_portletItemLocalService.fetchPortletItem(baseModelPK) == null) {
			return true;
		}

		return false;
	}

	@DeleteAfterTestRun
	private final List<PortletItem> _portletItems = new ArrayList<PortletItem>();
	@Inject
	private PortletItemLocalService _portletItemLocalService;
	@Inject
	private PortletItemUADTestHelper _portletItemUADTestHelper;
	@Inject(filter = "component.name=*.PortletItemUADAnonymizer")
	private UADAnonymizer _uadAnonymizer;
}