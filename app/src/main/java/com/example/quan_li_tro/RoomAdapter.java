package com.example.quan_li_tro;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private List<Room> roomListFull; // Bản sao lưu danh sách gốc
    private Context context;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Room room, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Room room, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
        this.roomListFull = new ArrayList<>(roomList);
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomName.setText(room.getName());
        holder.tvRoomPrice.setText("Giá: " + room.getPrice() + " VNĐ");
        
        if (room.isOccupied()) {
            holder.tvRoomStatus.setText("Đã thuê");
            holder.tvRoomStatus.setBackgroundColor(Color.RED);
        } else {
            holder.tvRoomStatus.setText("Còn trống");
            holder.tvRoomStatus.setBackgroundColor(Color.GREEN);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(room, position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(room, position);
                return true;
            }
            return false;
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(room, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    // Phương thức lọc dữ liệu
    public void filter(String text) {
        roomList.clear();
        if (text.isEmpty()) {
            roomList.addAll(roomListFull);
        } else {
            text = text.toLowerCase();
            for (Room item : roomListFull) {
                if (item.getName().toLowerCase().contains(text)) {
                    roomList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Cập nhật lại danh sách gốc khi có thay đổi (thêm/xóa/sửa)
    public void updateFullList() {
        this.roomListFull = new ArrayList<>(roomList);
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomPrice, tvRoomStatus;
        ImageButton btnDelete;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvRoomStatus = itemView.findViewById(R.id.tvRoomStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
