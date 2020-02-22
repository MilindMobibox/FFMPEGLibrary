package com.videomaker.photovideoeditorwithanimation.themes;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.mask.FinalMaskBitmap.EFFECT;

import java.util.ArrayList;

public enum THEMES {
    Shine("Shine") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList<>();
            mEffects.add(EFFECT.PIN_WHEEL);
            mEffects.add(EFFECT.SKEW_RIGHT_SPLIT);
            mEffects.add(EFFECT.SKEW_LEFT_SPLIT);
            mEffects.add(EFFECT.SKEW_RIGHT_MEARGE);
            mEffects.add(EFFECT.SKEW_LEFT_MEARGE);
            mEffects.add(EFFECT.FOUR_TRIANGLE);

            mEffects.add(EFFECT.SQUARE_IN);
            mEffects.add(EFFECT.SQUARE_OUT);
            mEffects.add(EFFECT.CIRCLE_LEFT_BOTTOM);
            mEffects.add(EFFECT.CIRCLE_IN);
            mEffects.add(EFFECT.DIAMOND_OUT);
            mEffects.add(EFFECT.HORIZONTAL_COLUMN_DOWNMASK);
            mEffects.add(EFFECT.RECT_RANDOM);
            mEffects.add(EFFECT.CROSS_IN);
            mEffects.add(EFFECT.DIAMOND_IN);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_2;
        }

        public int getThemeMusic() {
            return R.raw.hbdpartyversion;
        }
    },
    Love("Love") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList<>();
            mEffects.add(EFFECT.CIRCLE_IN);
            mEffects.add(EFFECT.HORIZONTAL_RECT);
            mEffects.add(EFFECT.HORIZONTAL_COLUMN_DOWNMASK);
            mEffects.add(EFFECT.LEAF);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_10;
        }

        public int getThemeMusic() {
            return R.raw.hpybudday;
        }
    },
    CIRCLE_IN("Circle In") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.CIRCLE_IN);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_6;
        }

        public int getThemeMusic() {
            return R.raw.hawabanke;
        }
    },
    CIRCLE_LEFT_BOTTOM("Circle Left Bottom") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.CIRCLE_LEFT_BOTTOM);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_9;
        }

        public int getThemeMusic() {
            return R.raw.tumhiaana;
        }
    },
    CIRCLE_OUT("Circle Out") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.CIRCLE_OUT);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_3;
        }

        public int getThemeMusic() {
            return R.raw.bekhayali;
        }
    },
    CIRCLE_RIGHT_BOTTOM("Circle Right Bottom") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.CIRCLE_RIGHT_BOTTOM);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_7;
        }

        public int getThemeMusic() {
            return R.raw.khairiyat;
        }
    },
    DIAMOND_IN("Diamond In") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.DIAMOND_IN);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_4;
        }

        public int getThemeMusic() {
            return R.raw.lehangatone;
        }
    },
    DIAMOND_OUT("Diamond out") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.DIAMOND_OUT);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_1;
        }

        public int getThemeMusic() {
            return R.raw.osakisakii;
        }
    },
    ECLIPSE_IN("Eclipse In") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.ECLIPSE_IN);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_8;
        }

        public int getThemeMusic() {
            return R.raw.tujekitnaachahnelagehum;
        }
    },
    FOUR_TRIANGLE("Four Triangle") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.FOUR_TRIANGLE);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_11;
        }

        public int getThemeMusic() {
            return R.raw.lamboghini;
        }
    },
    OPEN_DOOR("Open Door") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.OPEN_DOOR);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_5;
        }

        public int getThemeMusic() {
            return R.raw.symphony;
        }
    },
    PIN_WHEEL("Pin Wheel") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.PIN_WHEEL);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_15;
        }

        public int getThemeMusic() {
            return R.raw.fluteonmyway;
        }
    },
    RECT_RANDOM("Rect Random") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.RECT_RANDOM);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_13;
        }

        public int getThemeMusic() {
            return R.raw.iphone11promax;
        }
    },
    SKEW_LEFT_MEARGE("Skew Left Mearge") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.SKEW_LEFT_MEARGE);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_16;
        }

        public int getThemeMusic() {
            return R.raw.flutetumhiaana;
        }
    },
    SKEW_RIGHT_MEARGE("Skew Right Mearge") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.SKEW_RIGHT_MEARGE);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_12;
        }

        public int getThemeMusic() {
            return R.raw.pachtaogetone;
        }
    },
    SQUARE_OUT("Square Out") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.SQUARE_OUT);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_17;
        }

        public int getThemeMusic() {
            return R.raw.palpaldillkpass;
        }
    },
    SQUARE_IN("Square In") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.SQUARE_IN);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_14;
        }

        public int getThemeMusic() {
            return R.raw.bekhayali;
        }
    },
    VERTICAL_RECT("Vertical Rect") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> list = new ArrayList<>();
            list.add(EFFECT.VERTICAL_RECT);
            return list;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return new ArrayList<>();
        }

        public int getThemeDrawable() {
            return R.drawable.pcpe_img_animation_18;
        }

        public int getThemeMusic() {
            return R.raw.tumhiaana;
        }
    };

    String name;

    public abstract ArrayList<EFFECT> getTheme();

    public abstract ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList);

    public abstract int getThemeDrawable();

    public abstract int getThemeMusic();

    THEMES(String string) {
        this.name = "";
        this.name = string;
    }

    public String getName() {
        return this.name;
    }
}
