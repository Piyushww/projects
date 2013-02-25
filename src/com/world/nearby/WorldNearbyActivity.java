package com.world.nearby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class WorldNearbyActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	public static int THEME = R.style.Theme_Sherlock;
	private ImageView imgMapSearch, imgSearch, imgPark, imgSave, imgWNPic, imgInfo, imgIce, imgSos;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wn_startup);
//       startActionMode(new AnActionModeOfEpicProportions());
        
        imgMapSearch = (ImageView)findViewById(R.id.imgMapSearch);
        imgMapSearch.setOnClickListener(this);
        imgSearch = (ImageView)findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(this);
        imgPark = (ImageView)findViewById(R.id.imgPark);
        imgPark.setOnClickListener(this);
        imgIce = (ImageView)findViewById(R.id.imgIce);
        imgIce.setOnClickListener(this);
        imgSos = (ImageView)findViewById(R.id.imgSos);
        imgSos.setOnClickListener(this);
        imgInfo = (ImageView)findViewById(R.id.imgInfo);
        imgInfo.setOnClickListener(this);
        imgWNPic = (ImageView)findViewById(R.id.imgWNPic);
        
        Display display = getWindowManager().getDefaultDisplay();
		int screenheight = display.getHeight();
		
		if(screenheight > 800){
			imgWNPic.getLayoutParams().height = 590;
		}else if(screenheight <= 800 && screenheight > 480){
			imgWNPic.getLayoutParams().height = 373;
		}else if(screenheight <= 480 && screenheight >320){
			imgWNPic.getLayoutParams().height = 215;
		}else if(screenheight <= 320){
			imgWNPic.getLayoutParams().height = 191;
		}
		
        
    }
    
    private final class AnActionModeOfEpicProportions implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //Used to put dark icons on light action bar
            boolean isLight = THEME == R.style.Theme_Sherlock_Light;

            menu.add("Save")
                .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Search")
                .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Refresh")
                .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Save")
                .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Search")
                .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Refresh")
                .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Toast.makeText(WorldNearbyActivity.this, "Got click: " + item, Toast.LENGTH_SHORT).show();
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.imgMapSearch:
			startActivity(new Intent(this,MapSearchActivity.class));
			break;
		case R.id.imgSearch:
			startActivity(new Intent(this,SearchActivity.class));
			break;
		case R.id.imgPark:
			startActivity(new Intent(this,ParkActivity.class));
			break;
		case R.id.imgIce:
			startActivity(new Intent(this,IceActivity.class));
			break;
		case R.id.imgSos:
			startActivity(new Intent(this,SosActivity.class));
			break;
		case R.id.imgInfo:
			startActivity(new Intent(this,InfoActivity.class));
			break;

		default:
			break;
		}
	}
}