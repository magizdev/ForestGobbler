package com.magizdev.dayplan.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.util.INavigate;

public class DashboardFragment extends Fragment implements OnClickListener {
	private INavigate navigate;
	private boolean mShowingBack;

	public void setDataSource(INavigate naviate) {
		this.navigate = naviate;
	}

	public DashboardFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_report, container, false);

		Log.w("DashboardFragment", "onCreateView");
		Log.w("DashboardFragment", navigate.CurrentTitle());
		BaseChartFragment fragment = new PieChartFragment();
		fragment.setDataSource(navigate);
		fragment.setOnClick(this);

		if (savedInstanceState == null) {
			getActivity().getFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.w("DashboardFragment", "onResume");
		Log.w("DashboardFragment", navigate.CurrentTitle());
		BaseChartFragment fragment = new PieChartFragment();
		fragment.setDataSource(navigate);
		fragment.setOnClick(this);

		getActivity().getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();

	}

	private void flipCard() {
		if (mShowingBack) {
			mShowingBack = false;
			getActivity().getFragmentManager().popBackStack();
			return;
		}

		// Flip to the back.

		mShowingBack = true;

		BaseChartFragment fragment = new BarChartFragment();
		fragment.setDataSource(navigate);
		fragment.setOnClick(this);
		// Create and commit a new fragment transaction that adds the fragment
		// for the back of
		// the card, uses custom animations, and is part of the fragment
		// manager's back stack.

		getActivity().getFragmentManager().beginTransaction()

		// Replace the default fragment animations with animator resources
		// representing
		// rotations when switching to the back of the card, as well as animator
		// resources representing rotations when flipping back to the front
		// (e.g. when
		// the system Back button is pressed).
				.setCustomAnimations(R.animator.flip_right_in,
						R.animator.flip_right_out, R.animator.flip_left_in,
						R.animator.flip_left_out)

				// Replace any fragments currently in the container view with a
				// fragment
				// representing the next page (indicated by the just-incremented
				// currentPage
				// variable).
				.replace(R.id.container, fragment)

				// Add this transaction to the back stack, allowing users to
				// press Back
				// to get to the front of the card.
				.addToBackStack(null)

				// Commit the transaction.
				.commit();
	}

	@Override
	public void onClick(View v) {
		flipCard();
	}

}
