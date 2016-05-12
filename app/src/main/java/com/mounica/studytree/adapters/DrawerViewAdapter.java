package com.mounica.studytree.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mounica.studytree.R;
import com.mounica.studytree.models.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by ankur on 6/5/16.
 */
public class DrawerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_MENU_ITEMS = 1;

    private Context context;
    private ProfileEditListener profileEditListener;
    private MenuItemListener menuItemListener;

    private int[] menuIcons = {R.drawable.ic_home, R.drawable.ic_appointment, R.drawable.ic_settings, R.drawable.ic_profile, R.drawable.ic_help};
    private String[] menuTitles = {"Home", "Appointment", "Settings", "Update Profile", "Help"};

    public DrawerViewAdapter(Context context, ProfileEditListener profileEditListener, MenuItemListener menuItemListener) {
        this.context = context;
        this.profileEditListener = profileEditListener;
        this.menuItemListener = menuItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);

        View menuItemsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_menu_item_row, parent, false);
        if (viewType == TYPE_HEADER)
            return new HeaderViewHolder(headerView, profileEditListener);

        return new MenuItemHolder(menuItemsView, menuItemListener);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder)holder;
            String userImageLink = FeedAdapter.imageRootPath+User.getLoggedInUserImageLink(context);
            String userName = User.getLoggedInUserName(context);
            String userRegNo = String.valueOf(User.getLoggedInUserRegNo(context));

            Picasso.with(context).load(userImageLink).into(headerViewHolder.userImage);
            headerViewHolder.userName.setText(userName);
            headerViewHolder.userRegNo.setText(userRegNo);
        }
        else if (holder.getItemViewType() == TYPE_MENU_ITEMS) {
            MenuItemHolder menuItemHolder = (MenuItemHolder)holder;
            menuItemHolder.menuIcon.setImageDrawable(ContextCompat.getDrawable(context, menuIcons[position-1]));
            menuItemHolder.menuTitle.setText(menuTitles[position-1]);
        }
    }

    @Override
    public int getItemCount() {
        return menuTitles.length + 1;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;

        return TYPE_MENU_ITEMS;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        TextView userRegNo;
        Button userEditProfile;

        public HeaderViewHolder(View itemView, final ProfileEditListener profileEditListener) {
            super(itemView);
            userImage = (ImageView)itemView.findViewById(R.id.user_image);
            userName = (TextView)itemView.findViewById(R.id.user_name);
            userRegNo = (TextView)itemView.findViewById(R.id.user_reg_no);
            userEditProfile = (Button)itemView.findViewById(R.id.user_edit_profile);

            userEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    profileEditListener.onEditProfileClicked();
                }
            });
        }
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {

        RelativeLayout menuRow;
        ImageView menuIcon;
        TextView menuTitle;

        public MenuItemHolder(final View itemView, final MenuItemListener menuItemListener) {
            super(itemView);
            menuRow = (RelativeLayout)itemView.findViewById(R.id.drawer_menu_row);
            menuIcon = (ImageView)itemView.findViewById(R.id.drawer_menu_icon);
            menuTitle = (TextView) itemView.findViewById(R.id.drawer_menu_title);

            menuRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menuItemListener.onMenuItemClicked(itemView);
                }
            });
        }
    }

    public interface ProfileEditListener {
        void onEditProfileClicked();
    }

    public interface MenuItemListener {
        void onMenuItemClicked(View view);
    }
}
