package com.example.smart_laundromat_concept.ui.activities.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_laundromat_concept.R;
import com.example.smart_laundromat_concept.data.model.Notification;

import java.util.List;

/**
 * Adapter for displaying notifications in a RecyclerView.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Binds notification data to UI components</li>
 *     <li>Formats notification type into user-friendly labels</li>
 *     <li>Adjusts UI appearance (e.g., dim read notifications)</li>
 * </ul>
 *
 * <p><b>Design Notes:</b>
 * <ul>
 *     <li>Uses {@link Notification.Type} enum for type-safe handling</li>
 *     <li>Keeps UI logic separate from data model</li>
 * </ul>
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    // List of notifications to display
    private final List<Notification> notifications;

    /**
     * Creates adapter with notification data.
     *
     * @param notifications list of notifications
     */
    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    // -------------------------------------------------------------------------
    // Adapter Methods
    // -------------------------------------------------------------------------

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds a notification item to the UI.
     *
     * @param holder   view holder
     * @param position position in list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get current notification
        Notification notification = notifications.get(position);
        // Set message and timestamp
        holder.tvMessage.setText(notification.getMessage());
        holder.tvTime.setText(notification.getCreatedAt());
        // Convert raw type to enum and display label
        Notification.Type type = notification.getTypeEnum();
        holder.tvType.setText(getTypeLabel(type));

        // Dim read notifications slightly
        holder.itemView.setAlpha(notification.isRead() ? 0.6f : 1.0f);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * Converts notification type into user-friendly text.
     *
     * @param type notification type enum
     * @return readable label for UI
     */
    private String getTypeLabel(Notification.Type type) {
        if (type == null) return "";

        switch (type) {
            case BOOKING:
                return "Booking";
            case CYCLE_DONE:
                return "Collect laundry";
            case QUEUE:
                return "Machine available";
            case SYSTEM:
                return "Announcement";
            default:
                return "";
        }
    }

    // -------------------------------------------------------------------------
    // ViewHolder
    // -------------------------------------------------------------------------

    /**
     * Holds references to the views in each notification item.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvTime;
        TextView tvType;

        ViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.notification_item__message);
            tvTime    = itemView.findViewById(R.id.notification_item__time);
            tvType    = itemView.findViewById(R.id.notification_item__type);
        }
    }
}