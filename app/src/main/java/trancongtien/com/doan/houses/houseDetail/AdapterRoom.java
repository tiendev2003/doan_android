package trancongtien.com.doan.houses.houseDetail;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import trancongtien.com.doan.R;
import trancongtien.com.doan.houses.houseDetail.rooms.roomDetail.RoomDetailSystem;
import trancongtien.com.doan.model.Houses;
import trancongtien.com.doan.model.Rooms;

public class AdapterRoom extends RecyclerView.Adapter<AdapterRoom.HolderRooms> implements Filterable {
    HouseDetailSystem context;
    List<Rooms> roomsList;
    List<Rooms> roomsListOld;
    Houses houses;

    public AdapterRoom(HouseDetailSystem context, List<Rooms> roomsList, Houses houses) {
        this.context = context;
        this.roomsList = roomsList;
        this.roomsListOld = roomsList;
        this.houses = houses;
    }

    @NonNull
    @Override
    public HolderRooms onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_rooms, parent, false);
        return new AdapterRoom.HolderRooms(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRooms holder, int position) {
        Rooms rooms = roomsList.get(position);

        holder.txt_roomName.setText(rooms.getrName());
        // holder.txt_roomMembers.setText("Số người : 0/" + rooms.getrLimitTenants());
        holder.txt_roomFloor.setText("Tầng : " + rooms.getrFloorNumber());

        context.countTenants(rooms, holder.txt_roomMembers);
        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(rooms.getrPrice());
        holder.txt_roomFee.setText("Giá Phòng : " + formatter.format(cost) + " đ");

        holder.cv_roomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RoomDetailSystem.class);

                intent.putExtra("Data_Room_Parcelable", rooms);
                intent.putExtra("Data_RoomOfHouse_Parcelable", houses);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomsList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString();
                if (str.isEmpty()) {
                    roomsList = roomsListOld;
                } else {
                    List<Rooms> list = new ArrayList<>();
                    for (Rooms r : roomsListOld) {
                        list.add(r);
                    }
                    roomsList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = roomsList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                roomsList = (List<Rooms>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class HolderRooms extends RecyclerView.ViewHolder {
        CardView cv_roomItem;
        TextView txt_roomName, txt_roomMembers, txt_roomFloor, txt_roomFee;

        public HolderRooms(@NonNull View itemView) {
            super(itemView);

            txt_roomName = itemView.findViewById(R.id.txt_roomName);
            txt_roomMembers = itemView.findViewById(R.id.txt_roomMembers);
            txt_roomFloor = itemView.findViewById(R.id.txt_roomFloor);
            txt_roomFee = itemView.findViewById(R.id.txt_roomFee);

            cv_roomItem = itemView.findViewById(R.id.cv_roomItem);

        }
    }
}
