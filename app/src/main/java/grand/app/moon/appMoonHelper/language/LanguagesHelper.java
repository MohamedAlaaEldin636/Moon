package grand.app.moon.appMoonHelper.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

import grand.app.moon.core.MyApplication;
import grand.app.moon.presentation.base.utils.Constants;


/**
 * Created by mohamedatef on 1/12/19.
 */

public class LanguagesHelper {

    public static void changeLanguage(Context context ,String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());

        setLanguage(languageToLoad);
    }


    public static void setLanguage(String language) {
       SharedPreferences userDetails = MyApplication.instance.getSharedPreferences(Constants.LANGUAGE_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putString(Constants.LANGUAGE, language);
        editor.commit();
    }
    public static void setLanguage(Context context, String language) {
       SharedPreferences userDetails = context.getSharedPreferences(Constants.LANGUAGE_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putString(Constants.LANGUAGE, language);
        editor.commit();
    }

    public static String getCurrentLanguage() {
        SharedPreferences preferences = MyApplication.instance.getApplicationContext().getSharedPreferences(Constants.LANGUAGE_DATA, Context.MODE_PRIVATE);
        if (preferences.getString(Constants.LANGUAGE, "").length() > 0) {
            return preferences.getString(Constants.LANGUAGE, Constants.DEFAULT_LANGUAGE);
        } else {
            setLanguage(Constants.DEFAULT_LANGUAGE);
            return Constants.DEFAULT_LANGUAGE;
        }
    }
    public static String getCurrentLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.LANGUAGE_DATA, Context.MODE_PRIVATE);
        if (preferences.getString(Constants.LANGUAGE, "").length() > 0) {
            return preferences.getString(Constants.LANGUAGE, Constants.DEFAULT_LANGUAGE);
        } else {
            setLanguage(context, Constants.DEFAULT_LANGUAGE);
            return Constants.DEFAULT_LANGUAGE;
        }
    }
}
