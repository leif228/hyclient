package com.hy.client.activitys;

import com.hy.client.tools.ADFilterTool;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class NoAdWebViewClient extends WebViewClient{
	@Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (ADFilterTool.hasNotAd(url)) {
                return super.shouldInterceptRequest(view, url);
            } else {
                return new WebResourceResponse(null, null, null);
            }
    }
}
