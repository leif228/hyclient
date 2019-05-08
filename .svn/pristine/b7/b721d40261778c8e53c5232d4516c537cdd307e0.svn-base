package com.eyunda.third.activities.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.hy.client.R;

public class ContextMenu extends BaseActivity {

	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// int txtValue = EMMessage.Type.TXT.ordinal();
		int type = getIntent().getIntExtra("type", -1);
		// if (type == EMMessage.Type.TXT.ordinal()) {
		// setContentView(R.layout.eyd_chat_context_menu_for_text);
		// }
//		if (type == ChatAllHistoryActivity.REQUEST_CODE_MOREDEL_MENU) {
//			setContentView(R.layout.context_menu_for_moredel);
//		}
		if (type == NewChatAllHistoryActivity.REQUEST_CODE_MOREDEL_MENU_CHAT) {
			setContentView(R.layout.context_menu_for_moredel_chat);
		}
		if (type == NewChatAllHistoryActivity.REQUEST_CODE_MOREDEL_MENU_NOTIFY) {
			setContentView(R.layout.context_menu_for_moredel_notify);
		}

		if (type == ChatMessage.Type.TXT.ordinal()) {
			setContentView(R.layout.eyd_chat_context_menu_for_text);
		} else if (type == ChatMessage.Type.VOICE.ordinal()) {
			setContentView(R.layout.context_menu_for_voice);
		}

		position = getIntent().getIntExtra("position", -1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void copy(View view) {
		setResult(ChatActivity.RESULT_CODE_COPY,
				new Intent().putExtra("position", position));
		finish();
	}

	public void delete(View view) {
		setResult(ChatActivity.RESULT_CODE_DELETE,
				new Intent().putExtra("position", position));
		finish();
	}

	public void forward(View view) {
		setResult(ChatActivity.RESULT_CODE_FORWARD,
				new Intent().putExtra("position", position));
		finish();
	}
	
	
	public void delnf(View view) {
		setResult(NewChatAllHistoryActivity.RESULT_CODE_NOTIFYSDEL);
		finish();
	}
	public void delcr(View view) {
		setResult(NewChatAllHistoryActivity.RESULT_CODE_CHATROOMSDEL);
		finish();
	}

}
