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
 * RecyclerView adapter for displaying a list of notifications.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.tvMessage.setText(notification.getMessage());
        holder.tvTime.setText(notification.getCreatedAt());
        holder.tvType.setText(getTypeLabel(notification.getType()));

        // Dim read notifications slightly
        holder.itemView.setAlpha(notification.isRead() ? 0.6f : 1.0f);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * Returns a human-readable label for the notification type.
     *
     * @param type the raw type string from Supabase
     * @return a display label
     */
    private String getTypeLabel(String type) {
        if (type == null) return "";
        switch (type) {
            case Notification.TYPE_BOOKING:    return "Booking";
            case Notification.TYPE_CYCLE_DONE: return "Collect laundry";
            case Notification.TYPE_QUEUE:      return "Machine available";
            case Notification.TYPE_SYSTEM:     return "Announcement";
            default:                           return type;
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