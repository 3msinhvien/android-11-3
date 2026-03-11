package com.example.quan_li_tro;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnItemClickListener, RoomAdapter.OnItemLongClickListener {
    private List<Room> roomList = new ArrayList<>();
    private RoomAdapter adapter;
    private RecyclerView rvRooms;
    private ExtendedFloatingActionButton fabAddRoom;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRooms = findViewById(R.id.rvRooms);
        fabAddRoom = findViewById(R.id.fabAddRoom);
        searchView = findViewById(R.id.searchView);

        // Thêm dữ liệu mẫu
        roomList.add(new Room("101", "Phòng 101", 2500000, false, "", ""));
        roomList.add(new Room("102", "Phòng 102", 3000000, true, "Nguyễn Văn A", "0987654321"));
        roomList.add(new Room("103", "Phòng 103", 2800000, false, "", ""));

        adapter = new RoomAdapter(this, roomList);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        fabAddRoom.setOnClickListener(v -> showAddRoomDialog());

        // Thiết lập chức năng tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    private void showAddRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm phòng mới");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_room, null);
        builder.setView(view);

        EditText etRoomId = view.findViewById(R.id.etRoomId);
        EditText etRoomName = view.findViewById(R.id.etRoomName);
        EditText etRoomPrice = view.findViewById(R.id.etRoomPrice);
        EditText etTenantName = view.findViewById(R.id.etTenantName);
        EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        CheckBox cbIsOccupied = view.findViewById(R.id.cbIsOccupied);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String id = etRoomId.getText().toString().trim();
            String name = etRoomName.getText().toString().trim();
            String priceStr = etRoomPrice.getText().toString().trim();
            String tenantName = etTenantName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            boolean isOccupied = cbIsOccupied.isChecked();

            if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin cơ bản", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                Room newRoom = new Room(id, name, price, isOccupied, tenantName, phoneNumber);
                roomList.add(newRoom);
                adapter.updateFullList(); // Cập nhật danh sách tìm kiếm
                adapter.notifyItemInserted(roomList.size() - 1);
                rvRooms.scrollToPosition(roomList.size() - 1);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    public void onItemClick(Room room, int position) {
        // Menu tùy chọn: Sửa hoặc Xóa
        String[] options = {"Sửa", "Xóa"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thao tác cho " + room.getName());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                showUpdateRoomDialog(room, position);
            } else {
                showDeleteConfirmDialog(room, position);
            }
        });
        builder.show();
    }

    @Override
    public void onItemLongClick(Room room, int position) {
        showDeleteConfirmDialog(room, position);
    }

    private void showDeleteConfirmDialog(Room room, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa phòng này không?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            roomList.remove(position);
            adapter.updateFullList(); // Cập nhật danh sách tìm kiếm
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, roomList.size());
            Toast.makeText(this, "Đã xóa phòng", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Không", null);
        builder.show();
    }

    private void showUpdateRoomDialog(Room room, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa thông tin phòng");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_room, null);
        builder.setView(view);

        EditText etRoomId = view.findViewById(R.id.etRoomId);
        EditText etRoomName = view.findViewById(R.id.etRoomName);
        EditText etRoomPrice = view.findViewById(R.id.etRoomPrice);
        EditText etTenantName = view.findViewById(R.id.etTenantName);
        EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        CheckBox cbIsOccupied = view.findViewById(R.id.cbIsOccupied);

        // Đổ dữ liệu cũ vào
        etRoomId.setText(room.getId());
        etRoomId.setEnabled(false);
        etRoomName.setText(room.getName());
        etRoomPrice.setText(String.valueOf(room.getPrice()));
        etTenantName.setText(room.getTenantName());
        etPhoneNumber.setText(room.getPhoneNumber());
        cbIsOccupied.setChecked(room.isOccupied());

        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String name = etRoomName.getText().toString().trim();
            String priceStr = etRoomPrice.getText().toString().trim();
            String tenantName = etTenantName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            boolean isOccupied = cbIsOccupied.isChecked();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                room.setName(name);
                room.setPrice(price);
                room.setOccupied(isOccupied);
                room.setTenantName(tenantName);
                room.setPhoneNumber(phoneNumber);
                
                adapter.updateFullList(); // Cập nhật danh sách tìm kiếm
                adapter.notifyItemChanged(position);
                Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showDeleteConfirmDialog(Room room, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng " + room.getName() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    roomList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, roomList.size());
                    Toast.makeText(this, "Đã xóa phòng", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
