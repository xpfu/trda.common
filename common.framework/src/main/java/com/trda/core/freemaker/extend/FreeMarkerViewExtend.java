package com.trda.core.freemaker.extend;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.trda.common.model.UUser;
import com.trda.common.utils.LoggerUtils;
import com.trda.core.shiro.token.manager.TokenManager;
import com.trda.core.statics.Constant;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年8月1日 上午11:32:40
 */
public class FreeMarkerViewExtend extends FreeMarkerView {

	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) {

		try {
			super.exposeHelpers(model, request);
		} catch (Exception e) {
			LoggerUtils.fmtError(FreeMarkerViewExtend.class, e, "FreeMarkerViewExtend 加载父类出现异常。请检查。");
		}
		model.put(Constant.CONTEXT_PATH, request.getContextPath());
		model.putAll(FreeMakerUtils.initMap);
		UUser token = TokenManager.getToken();
		// String ip = IPUtils.getIP(request);
		if (TokenManager.isLogin()) {
			model.put("token", token);// 登录的token
		}

		model.put("_time", new Date().getTime());
		model.put("NOW_YEAY", Constant.NOW_YEAY);// 今年

		model.put("_v", Constant.VERSION);// 版本号，重启的时间
		model.put("cdn", Constant.DOMAIN_CDN);// CDN域名
		model.put("basePath", request.getContextPath());// base目录。

	}
}
