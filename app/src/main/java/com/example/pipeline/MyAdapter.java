package com.example.pipeline;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>implements Filterable {

    //declaring variables to be used in this file
    private Context context;
    private ArrayList<Event> events;
    private ArrayList<Event> filteredEvents;
    private EventsReaderDatabaseHelper eventsReaderDatabaseHelper;
    private selectedEvent selectedEvent;

    //constructor taking context, arraylist of events and selected event
    public MyAdapter(Context pContext, ArrayList<Event> pEvents, selectedEvent selectedEvent){
        //setting context
        context = pContext;
        //setting events arraylist to the argument passed to the constructor
        events = pEvents;

        //instantiating eventsReaderDatabaseHelper passing the current context
        eventsReaderDatabaseHelper = new EventsReaderDatabaseHelper(pContext);
        //assigning selectedEvent to the selectedEvent argument
        this.selectedEvent = selectedEvent;
        //assigning the filteredEvents variable to the events arraylist passed as an argument
        this.filteredEvents = events;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating context
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflating view locating the template to be used for each record in the layout resource file
        View view = inflater.inflate(R.layout.event_record, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {

        //getting the current element from the arraylist events
        final Event event = events.get(position);

        //setting the title using the current event's getTitle() method from the eventsReaderDatabaseHelper
        holder.title.setText(events.get(position).getTitle());
        //setting the date using the current event's getDate() method from the eventsReaderDatabaseHelper
        holder.date.setText(events.get(position).getDate());
        //obtaining the color and assigning it to the colorAttribute variable
        final String colorAttribute = events.get(position).getColor();
        //setting the background color to the color attribute obtained above
        holder.background.setBackgroundColor(Color.parseColor(colorAttribute));

        //getting today's date
        Date today = Calendar.getInstance().getTime();
        //instantiating date format
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        //declaring date variable
        Date dbDate;
        try {
            //attempt to parse the date obtained from the database
            dbDate = format.parse(events.get(position).getDate());
            //if the date is before today's date
            if(dbDate.before(today)){
                //set the background to white
                holder.background.setBackgroundColor(Color.parseColor("#FFFFFF"));
                //set the title text to red
                holder.title.setTextColor(Color.parseColor("#FF0000"));
                //set the date text to red
                holder.date.setTextColor(Color.parseColor("#FF0000"));
                //set the delete icon to a red one
                holder.delete.setImageResource(R.drawable.ic_delete_forever_red_24dp);
                //make the arrow to view details invisible not allowing the user to view details but to simply delete
                holder.details.setVisibility(View.INVISIBLE);
            }

        } catch (ParseException e) {
            //in the event of an exception parsing the date print stack trace
            e.printStackTrace();
        }

        //setting an onclick listener for the delete button
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove from the events arraylist
                events.remove(events.get(position));
                //remove it from the database by means of the eventsReaderDatabaseHelper
                eventsReaderDatabaseHelper.deleteEvent(event.getID());
                //notify item was removed at the position in question
                notifyItemRemoved(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public Filter getFilter() {
        //perform filtering to search through the arraylist of events
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //instantiating a new filter
                FilterResults filterResults = new FilterResults();
                //if the char sequence is null or 0 set size and values accordingly
                if(constraint == null | constraint.length()==0){
                    filterResults.count = filteredEvents.size();
                    filterResults.values = filteredEvents;
                }else{
                    //if the char sequence is no null or 0:
                    //convert the text to lowercase
                    String searchChar = constraint.toString().toLowerCase();
                    //declare a new arraylist
                    ArrayList<Event> results = new ArrayList<>();

                    //traverse the arraylist filteredEvents as declared at the top of the class loaded with all current
                    //events on display
                    for (Event event: filteredEvents){
                        //if the title as converted to lowercase is equal to the char sequence being used to search
                        //add the event to the results arraylist
                        if(event.getTitle().toLowerCase().contains(searchChar)){
                            //add to results arraylist
                            results.add(event);
                        }
                    }

                    //set count to the size of the results arraylist
                    filterResults.count = results.size();
                    //set the filter results arraylist values to the results arraylist
                    filterResults.values = results;
                }
                //return the results of the filter
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                //set events arraylist to the results values obtained from the search
                events = (ArrayList<Event>) results.values;
                //notify that the data set changed to refresh the current screen
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    public interface selectedEvent{
        void selectedEvent(Event event);
    }


    //MyViewHolder class is used to locate the textviews and image views as a template for each event on display on the home screen
    public class MyViewHolder extends RecyclerView.ViewHolder{

        //declaring variables to be used
        TextView title;
        TextView date;
        ImageView delete;
        ImageView details;
        androidx.constraintlayout.widget.ConstraintLayout background;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //locating title textView in the respective layout resource file named event_record
            //which is a template for all events on display
            title = itemView.findViewById(R.id.EventTitleTextView);

            //locating date textView in the respective layout resource file named event_record
            //which is a template for all events on display
            date = itemView.findViewById(R.id.dateTextView);

            //locating background textView in the respective layout resource file named event_record
            //which is a template for all events on display
            background = itemView.findViewById(R.id.recordBackground);

            //locating delete imageView in the respective layout resource file named event_record
            //which is a template for all events on display
            delete = itemView.findViewById(R.id.deleteImageView);

            //locating details imageView in the respective layout resource file named event_record
            //which is a template for all events on display
            details = itemView.findViewById(R.id.detailsImageView);

            //setting an onclick listener for the details imageview
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //on click selected event is instantiated with the position obtained from the adapter
                    //locating the selected event in the arraylist events
                    selectedEvent.selectedEvent(events.get(getAdapterPosition()));
                }
            });
        }
    }

}
