package com.crossapps.petpal.Util;//package com.webpulse.crm.Util;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Typeface;
//import android.text.style.CharacterStyle;
//import android.text.style.StyleSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.common.data.DataBufferUtils;
//import com.google.android.gms.location.places.AutocompleteFilter;
//import com.google.android.gms.location.places.AutocompletePrediction;
//import com.google.android.gms.location.places.AutocompletePredictionBuffer;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.webpulse.crm.R;
//
//import java.util.ArrayList;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by Kanwar on 30/12/2015.
// */
//public class LocationList extends BaseAdapter implements Filterable {
//
//
//    Context context;
//    Activity activity;
//    LayoutInflater inflater;
//
//    // private Filter filter = new CustomFilter();
//   /* public LocationList(Activity activity,
//                        ArrayList<GetVendorLocation> getlist) {
//        this.activity = activity;
//
//        context = activity.getApplicationContext();
//        this.inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.getlist = getlist;
//
//        // this.vendordetail = vendorcardetail;
//
//
//    }*/
//    private static final String TAG = "PlaceAutocompleteAdapter";
//    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
//    /**
//     * Current results returned by this adapter.
//     */
//    private ArrayList<AutocompletePrediction> mResultList;
//
//    /**
//     * Handles autocomplete requests.
//     */
//    private GoogleApiClient mGoogleApiClient;
//
//    /**
//     * The bounds used for Places Geo Data autocomplete API requests.
//     */
//    private LatLngBounds mBounds;
//
//    /**
//     * The autocomplete filter used to restrict queries to a specific set of place types.
//     */
//    private AutocompleteFilter mPlaceFilter;
//    public LocationList(Context context, GoogleApiClient googleApiClient,
//                        LatLngBounds bounds, AutocompleteFilter filter) {
//        this.context=context;
//        mGoogleApiClient = googleApiClient;
//        mBounds = bounds;
//        mPlaceFilter = filter;
//        this.inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//    @Override
//    public int getCount() {
//        //  return suggestions.size();
//        if(mResultList!=null && mResultList.size()>0) {
//            return mResultList.size();
//        }else {
//            return 0;
//        }
//    }
//
//    /* @Override
//     public Object getItem(int position) {
//       //  return suggestions.get(position);
//         return mResultList.get(position);
//     }*/
//    @Override
//    public AutocompletePrediction getItem(int position) {
//        return mResultList.get(position);
//    }
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        convertView = inflater.inflate(R.layout.auto_row,null);
//        TextView autotext=(TextView)convertView.findViewById(R.id.autotext);
//        TextView autotext2=(TextView)convertView.findViewById(R.id.autotext2);
//        AutocompletePrediction item = getItem(position);
//
//        autotext.setText(item.getPrimaryText(STYLE_BOLD));
//        autotext2.setText(item.getSecondaryText(STYLE_BOLD));
//        return convertView;
//    }
//
//    /*@Override
//    public Filter getFilter() {
//        return filter;
//    }
//    private class CustomFilter extends Filter {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            suggestions.clear();
//
//            if (getlist != null && constraint != null) { // Check if the Original List and Constraint aren't null.
//                for (int i = 0; i < getlist.size(); i++) {
//                    if (getlist.get(i).getVendorlocation().toLowerCase().contains(constraint)) { // Compare item in original comment if it contains constraints.
//                        suggestions.add(getlist.get(i)); // If TRUE add item in Suggestions.
//                    }
//                }
//            }
//            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
//            results.values = suggestions;
//            results.count = suggestions.size();
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            if (results.count > 0) {
//                notifyDataSetChanged();
//            } else {
//                notifyDataSetInvalidated();
//            }
//        }
//    }*/
//
//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();
//                // Skip the autocomplete query if no constraints are given.
//                if (constraint != null) {
//                    // Query the autocomplete API for the (constraint) search string.
//                    mResultList = getAutocomplete(constraint);
//                    if (mResultList != null) {
//                        // The API successfully returned results.
//                        results.values = mResultList;
//                        results.count = mResultList.size();
//                    }
//                }
//                return results;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if (results != null && results.count > 0) {
//                    // The API returned at least one result, update the data.
//                    notifyDataSetChanged();
//                } else {
//                    // The API did not return any results, invalidate the data set.
//                    notifyDataSetInvalidated();
//                }
//            }
//
//            @Override
//            public CharSequence convertResultToString(Object resultValue) {
//                // Override this method to display a readable result in the AutocompleteTextView
//                // when clicked.
//                if (resultValue instanceof AutocompletePrediction) {
//                    return ((AutocompletePrediction) resultValue).getFullText(null);
//                } else {
//                    return super.convertResultToString(resultValue);
//                }
//            }
//        };
//    }
//    /**
//     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
//     * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
//     * objects to store the Place ID and description that the API returns.
//     * Returns an empty comment if no results were found.
//     * Returns null if the API client is not available or the query did not complete
//     * successfully.
//     * This method MUST be called off the main UI thread, as it will block until data is returned
//     * from the API, which may include a network request.
//     *
//     * @param constraint Autocomplete query string
//     * @return Results from the autocomplete API or null if the query was not successful.
//     * @see Places#GEO_DATA_API#getAutocomplete(CharSequence)
//     * @see AutocompletePrediction#freeze()
//     */
//    @SuppressLint("LongLoggerTag")
//    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
//        if (mGoogleApiClient.isConnected()) {
//            Logger.d(TAG, "Starting autocomplete query for: " + constraint);
//
//            // Submit the query to the autocomplete API and retrieve a PendingResult that will
//            // contain the results when the query completes.
//            PendingResult<AutocompletePredictionBuffer> results =
//                    Places.GeoDataApi
//                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
//                                    mBounds, mPlaceFilter);
//
//            // This method should have been called off the main UI thread. Block and wait for at most 60s
//            // for a result from the API.
//            AutocompletePredictionBuffer autocompletePredictions = results
//                    .await(60, TimeUnit.SECONDS);
//
//            // Confirm that the query completed successfully, otherwise return null
//            final Status status = autocompletePredictions.getStatus();
//            if (!status.isSuccess()) {
//              //  Toast.makeText(context, "Error contacting API: " + status.toString(),
//                //        Toast.LENGTH_SHORT).show();
//                Logger.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
//                autocompletePredictions.release();
//                return null;
//            }
//
//            Logger.d(TAG, "Query completed. Received " + autocompletePredictions.getCount()
//                    + " predictions.");
//
//            // Freeze the results immutable representation that can be stored safely.
//            return DataBufferUtils.freezeAndClose(autocompletePredictions);
//        }
//        Logger.e(TAG, "Google API client is not connected for autocomplete query.");
//        return null;
//    }
//}
