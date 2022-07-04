package sensetime.senseme.com.effects.entity;

/**
 * Created by luguoqiang
 */

public class StyleContentEntity {

    private final String cityName;
    private final int cityIcon;
    private final String temperature;

    public StyleContentEntity(String cityName, int cityIcon, String temperature) {
        this.cityName = cityName;
        this.cityIcon = cityIcon;
        this.temperature = temperature;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityIcon() {
        return cityIcon;
    }

    public String getTemperature() {
        return temperature;
    }

}
