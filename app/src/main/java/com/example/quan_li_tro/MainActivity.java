package com.example.quan_li_tro;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Room> roomList = new ArrayList<>();
    private RoomAdapter adapter;
    private RecyclerView rvRooms;
    private ExtendedFloatingActionButton fabAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRooms = findViewById(R.id.rvRooms);
        fabAddRoom = findViewById(R.id.fabAddRoom);

        // Thêm dữ liệu mẫu để app không bị trắng khi vừa chạy
        roomList.add(new Room("101", "Phòng 101", 2500000, false, "", ""));
        roomList.add(new Room("102", "Phòng 102", 3000000, true, "Nguyễn Văn A", "0987654321"));
        roomList.add(new Room("103", "Phòng 103", 2800000, false, "", ""));

        adapter = new RoomAdapter(this, roomList);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        fabAddRoom.setOnClickListener(v -> showAddRoomDialog());
    }

    private void showAddRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm phòng mới");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_room, null);
        builder.setView(view);

        EditText etRoomId = view.findViewById(R.id.etRoomId);
        EditText etRoomName = view.findViewById(R.id.etRoomName);
        EditText etRoomPrice = view.findViewById(R.id.etRoomPrice);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String id = etRoomId.getText().toString().trim();
            String name = etRoomName.getText().toString().trim();
            String priceStr = etRoomPrice.getText().toString().trim();

            if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                Room newRoom = new Room(id, name, price, false, "", "");
                roomList.add(newRoom);
                adapter.notifyItemInserted(roomList.size() - 1);
                rvRooms.scrollToPosition(roomList.size() - 1);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
