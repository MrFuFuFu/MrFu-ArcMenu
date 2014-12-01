package com.mrfu.arcmenu;

import com.mrfu.arcmenu.view.ArcMenu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/***
 * @author MrFu
 */
public class MainActivity extends Activity {
	private Context mContext = MainActivity.this;
	private ListView listview;
	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_createring, R.drawable.composer_customring};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ListView)findViewById(R.id.listview);
		View view = View.inflate(this, R.layout.listview_footer, null);
		listview.addFooterView(view);
		listview.setAdapter(new ListViewAdapter());
		ArcMenu arcMenu = (ArcMenu) findViewById(R.id.arc_menu_add);
		initArcMenu(arcMenu, ITEM_DRAWABLES);
	}
	
	private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(itemDrawables[i]);
            final int position = i;
            menu.addItem(position, item, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
	
	class ListViewAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return 9;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 9;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(mContext, R.layout.listview_item, null);
				holder.textView = (TextView)findViewById(R.id.textview);
				convertView.setTag(holder);
			}else{
				holder = (Holder)convertView.getTag();
			}
			return convertView;
		}
		class Holder{
			private TextView textView;
		}
	}
}
