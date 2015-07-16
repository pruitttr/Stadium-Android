package com.stadiumse.stadiumstockexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class MainActivity extends Activity
        implements DashboardFragment.OnFragmentInteractionListener,
        StocksFragment.OnFragmentInteractionListener {

    //Debugging Tag.
    private static final String TAG = "Main Activity";

    //Vars
    private Toolbar toolbar;
    private ImageButton fab;
    private DatabaseHandler db;

    //Vars for fab animation.
    public final static float FAB_SCALE_FACTOR      = 20f;
    public final static int FAB_ANIMATION_DURATION  = 300;
    public final static int FAB_MINIMUM_X_DISTANCE  = 250;

    //Height integers to manage the arithmetic behind
    //toolbar_dashboard animation
    private int toolbarMinHeight;
    private int toolbarCurHeight;

    //X & Y coordinates for fab starting position.
    private float fabX;
    private float fabY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiation of private vars
        toolbar = (Toolbar) findViewById(R.id.toolbar_dashboard);
        fab = (ImageButton) findViewById(R.id.fab_stocks_top);
        db = new DatabaseHandler(this);

        //Use more flexible toolbar_dashboard layout as our ActionBar and remove
        //its title.
        setActionBar(toolbar);
        if (getActionBar() != null)
        getActionBar().setDisplayShowTitleEnabled(false);

        //Create an oval outline for the fabs
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {

            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        };

        //FabTop is the user viewable fab with the stock logo.  It appears
        //above the toolbar_dashboard.  fab is invisible and under the toolbar_dashboard.
        //The following sets the shape and Z-shadow to be round.
        fab.setOutlineProvider(viewOutlineProvider);
        fab.setClipToOutline(true);

        //Listen for clicks to start animation
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                stocksAnimation();
                fab.setClickable(false);
            }
        });

        //Set up the Dashboard onCreate.
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_content, new DashboardFragment());
        fragmentTransaction.commit();

        //Set circular user image
        ImageView imageView = (ImageView) findViewById(R.id.imageview_toolbar_userpic);
        Bitmap userPic = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        imageView.setImageBitmap(userPic);

        populateToolbar();
    }

    public void onFragmentInteraction(Uri uri){
        //Required fragment interaction callback
        //you can leave it empty
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                DashboardAnimation();
                return true;
            case R.id.action_settings:
                return true;
        }
        return false;
    }

    private void populateToolbar() {
        Dashboard dashboard = db.getDashboard(1094);

        TextView textview_toolbar_current_value = (TextView) findViewById(R.id.textview_toolbar_current_value);
        TextView textview_toolbar_username = (TextView) findViewById(R.id.textview_toolbar_username);
        TextView textview_toolbar_league_info = (TextView) findViewById(R.id.textview_toolbar_league_info);
        ImageView imageview_toolbar_userpic = (ImageView) findViewById(R.id.imageview_toolbar_userpic);

        if (dashboard !=null) {

            textview_toolbar_current_value.setText(dashboard.getPortfolioValue());
            textview_toolbar_league_info.setText("League: # | " + dashboard.getFavTicker() + ": #" +
                    dashboard.getFavFanbaseRank() + " of " + dashboard.getStockCount());
            Picasso.with(this).load("https://res.cloudinary.com/bus-productions/" +
                    "image/upload/w_160,h_160,c_limit,c_fill/user_827_7.jpg")
                    .into(imageview_toolbar_userpic);
        }

    }

    /**This handles toolbar_dashboard, fab, and fragment animations when the
     * user clicks the fab.  Both the toolbar_dashboard and Dashboard fragment
     * slide away from one another, while the fab follows a curved
     * path and expands to fill the fragment container.  After the
     * animation, Stocks Fragment becomes visible.
     */
    //TODO: Fix the stock logo animation on fab
    //TODO: Fix the bug where dashboard view gets stuck underneath
    private void stocksAnimation () {
        //Switching DashboardFragment with StocksFragment using a custom animation
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction
                //no animation on enter, slide down on exit
                .setCustomAnimations(0, R.anim.slide_down_full)
                .replace(R.id.container_content, new StocksFragment())
                .commit();

        //Toolbar height is 56dp according to Material Design spec
        //This gets the real pixel value from dp.
        toolbarMinHeight = toolbar.getMinimumHeight();

        //Subtracts the 56dp pixel conversion from the Toolbar's current
        //height so that interpolatedTime at zero gives us the minimum
        //Toolbar.
        toolbarCurHeight = toolbar.getHeight()-toolbarMinHeight;

        //Toolbar animation from 232dp to 56dp over 400 milliseconds.
        Animation toolbarAnimation = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                FrameLayout.LayoutParams toolbarParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
                toolbarParams.height =
                        (int)(toolbarMinHeight + (toolbarCurHeight - (interpolatedTime * toolbarCurHeight)));
                toolbar.setLayoutParams(toolbarParams);
            }
        };
        toolbarAnimation.setDuration(400); // in ms
        toolbar.startAnimation(toolbarAnimation);

        //Give us that up navigation arrow!
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        //Thus starts the fab animation code.  Setting fabX and
        //fabY allows the fab to return to its original position
        //later.
        fabX = fab.getX();
        fabY = fab.getY();

        //Starting point of fab so that we can calculate
        //distance from this point to determine when to
        //stop the path and start the scale animation.
        final float startX = fab.getX();

        //Curved path.  See class for more details.
        FabAnimatorPath path = new FabAnimatorPath();
        path.moveTo(0, 0);
        path.curveTo(-200, 200, -400, 100, -600, 50);

        //clear the stock image, so it doesn't expand with the fab
        fab.setImageResource(android.R.color.transparent);
        //fix a bug from the use of fillAfter in the fab_pickup animation
        fab.clearAnimation();

        RelativeLayout toolbarInfoLayout = (RelativeLayout) findViewById(R.id.layout_toolbar_info);
        toolbarInfoLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_set));

        //Custom animator for fab.
        ObjectAnimator fabAnimator = ObjectAnimator.ofObject(this, "fabLoc",
                new PathEvaluator(), path.getPoints().toArray());
        fabAnimator.setInterpolator(new AccelerateInterpolator());
        fabAnimator.setDuration(FAB_ANIMATION_DURATION).start();

        //We want to maintain the fab size until it breaches our predefined
        //minimum distance variable.  At that point, the fab expands to fill
        //the view and reveal the Stocks Fragment
        fabAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //Is it beyond the minimum distance?
                if (Math.abs(startX - fab.getX()) > FAB_MINIMUM_X_DISTANCE) {
                    //If so, then explode in 300 milliseconds and listen for
                    //when it finishes.
                    fab.animate()
                            .scaleXBy(FAB_SCALE_FACTOR)
                            .scaleYBy(FAB_SCALE_FACTOR)
                            .setListener(fabExpandListener)
                            .setDuration(FAB_ANIMATION_DURATION);

                }
            }
        });
    }

    //When fab completely covers the view, we want to reveal the Stocks Fragment.
    //and hide fab
    private AnimatorListenerAdapter fabExpandListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            //Make the invisible Stocks Fragment visible without producing nullpointerexception
            try {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.container_content);
                View view = fragment.getView();
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout_stocks_fragment);
                fab.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                Log.e(TAG, "Could not get frame layout from Stocks Fragment");
            }
        }
    };

    //This method is required by our custom ObjectAnimator.
    public void setFabLoc(PathPoint newLoc) {
        fab.setTranslationX(newLoc.mX);
        fab.setTranslationY(newLoc.mY);
    }

    /**This handles toolbar_dashboard, fab, and fragment animations when the
     * user clicks the up navigation arrow while viewing Stocks Fragment.
     * Both the toolbar_dashboard and Dashboard fragment slide together, and
     * the fab grows in from the crease after they meet.
     */
    private void DashboardAnimation () {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up_full, R.anim.fade_out);
        fragmentTransaction.replace(R.id.container_content, new DashboardFragment());
        fragmentTransaction.commit();

        RelativeLayout toolbarInfoLayout = (RelativeLayout) findViewById(R.id.layout_toolbar_info);
        toolbarInfoLayout.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in_set));

        //Bring back the extended toolbar_dashboard for Dashboard Fragment
        Animation toolbarAnimation = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                FrameLayout.LayoutParams toolbarParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
                toolbarParams.height = (int) (toolbarMinHeight + (interpolatedTime * toolbarCurHeight));
                toolbar.setLayoutParams(toolbarParams);
            }
        };
        toolbarAnimation.setDuration(400); // in ms
        toolbarAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //Shrink fab and put him back where he started.
                fab.animate()
                        .x(fabX)
                        .y(fabY)
                        .scaleXBy(-FAB_SCALE_FACTOR)
                        .scaleYBy(-FAB_SCALE_FACTOR);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //An animation for FabTop to zoom back into place.
                Animation fab_pickup = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_pickup);
                fab.setVisibility(View.VISIBLE);
                fab.setImageResource(R.drawable.ic_stock_logo);
                fab.setElevation(6);
                fab.setClickable(true);
                fab.startAnimation(fab_pickup);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        toolbar.startAnimation(toolbarAnimation);

        //Take away the up navigation arrow.
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(false);
        }


    }

}
