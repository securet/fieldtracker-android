package com.oppo.sfamanagement.fragment;

import android.app.DialogFragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oppo.sfamanagement.R;

/**
 * Created by allsmartlt218 on 30-12-2016.
 */

public class PromoterImageDialog extends Fragment {
    private ImageView ivPhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.promoter_image_fragment,container,false);
        ivPhoto = (ImageView) view.findViewById(R.id.ivPromoterImage);

        return view;
    }
}
