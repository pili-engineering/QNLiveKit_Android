package sensetime.senseme.com.effects.entity

import sensetime.senseme.com.effects.R

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/16/21 7:51 PM
 */
object StyleStation {

//    open fun getStyleList(): ArrayList<StyleTitleEntity> {
//        val list = ArrayList<StyleTitleEntity>()
//        list.add(StyleTitleEntity("自然", getZiRan()))
//        list.add(StyleTitleEntity("轻妆", null))
//        list.add(StyleTitleEntity("流行", getForecasts()))
//        return list
//    }

    private fun getForecasts(): List<StyleContentEntity> {
        return listOf(
            StyleContentEntity(
                "Pisa",
                R.drawable.ic_style_tianran,
                "16"
            ),
            StyleContentEntity(
                "Paris",
                R.drawable.ic_style_shuiwu,
                "14"
            ),
            StyleContentEntity(
                "New York",
                R.drawable.ic_style_tianran,
                "9"
            ),
            StyleContentEntity(
                "Rome",
                R.drawable.ic_style_tianran,
                "18"
            ),
            StyleContentEntity(
                "London",
                R.drawable.ic_style_tianran,
                "6"
            )
        )
    }

    private fun getZiRan(): List<StyleContentEntity> {
        return listOf(
                StyleContentEntity(
                        "Pisa",
                        R.drawable.ic_style_naiyou,
                        "16"
                ),
                StyleContentEntity(
                        "Paris",
                        R.drawable.ic_style_tianran,
                        "14"
                ),
                StyleContentEntity(
                        "New York",
                        R.drawable.ic_style_baixi,
                        "9"
                ),
                StyleContentEntity(
                        "Rome",
                        R.drawable.ic_style_yuanqi,
                        "18"
                ),
                StyleContentEntity(
                        "London",
                        R.drawable.ic_style_shuiwu,
                        "6"
                )
        )
    }
}