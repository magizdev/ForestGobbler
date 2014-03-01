package com.magizdev.dayplan.versionone;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.magizdev.dayplan.R;

public class DrawerActivity extends FragmentActivity {
	private String[] mPageTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private android.support.v4.app.Fragment chartFragment;
	private Fragment loginFragment;
	private Fragment dayPlanFragment;
	private Fragment backlogItemFragment;
	private int position;
	private DrawerAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);

		mTitle = mDrawerTitle = getTitle();
		mPageTitles = getResources().getStringArray(R.array.Pages);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer

		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		adapter = new DrawerAdapter(this);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setDisplayShowTitleEnabled(true);
				getActionBar().setNavigationMode(
						ActionBar.NAVIGATION_MODE_STANDARD);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		switch (position) {
		case 0:
			menu.clear();
			break;
		case 1:
			inflater.inflate(R.menu.activity_backlog_item, menu);
			break;
		case 2:
			inflater.inflate(R.menu.activity_dashboard, menu);
		default:
			break;
		}
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_pickup:
			((BacklogItemFragment) backlogItemFragment).addTasks();
			selectItem(0);
			invalidateOptionsMenu();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		FragmentManager fragmentManager = getFragmentManager();
		android.support.v4.app.FragmentManager supportFragmentManager = getSupportFragmentManager();
		android.support.v4.app.Fragment chart = getSupportFragmentManager()
				.findFragmentByTag("chart");
		Fragment other = fragmentManager.findFragmentByTag("other");
		if (other != null) {
			fragmentManager.beginTransaction().show(other).commit();
		}
		if (chart != null) {
			supportFragmentManager.beginTransaction().hide(chart).commit();
		}
		if (loginFragment == null) {
			loginFragment = new LoginFragment();
		}
		if (chartFragment == null) {
			chartFragment = new ChartFragment();
		}
		if (dayPlanFragment == null) {
			dayPlanFragment = new DayPlanFragment();
		}
		if (backlogItemFragment == null) {
			backlogItemFragment = new BacklogItemFragment();
		}
		switch (position) {
		case 0:
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, dayPlanFragment, "other")
					.commit();
			break;
		case 1:
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, backlogItemFragment, "other")
					.commit();
			break;
		case 2:
			if (other != null) {
				fragmentManager.beginTransaction().hide(other).commit();
			}

			if (chart != null) {
				supportFragmentManager.beginTransaction().show(chart).commit();

			} else {
				supportFragmentManager.beginTransaction()
						.replace(R.id.content_frame, chartFragment, "chart")
						.commit();
			}
			break;
		case 3:
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, loginFragment, "other")
					.commit();
			break;
		default:
			break;
		}

		mDrawerList.setItemChecked(position, true);
		adapter.select(position);
		setTitle(mPageTitles[position]);
		this.position = position;
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
