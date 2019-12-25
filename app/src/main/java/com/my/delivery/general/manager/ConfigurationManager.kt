package com.my.delivery.general.manager

import com.my.delivery.general.entity.config.APIConfiguration
import com.my.delivery.general.entity.config.FeatureConfiguration

open class ConfigurationManager {

    open var apiConfiguration:APIConfiguration? = null
    open var featureConfiguration:FeatureConfiguration? = null

    companion object {
        private var configurationManager:ConfigurationManager? =null

        val instance:ConfigurationManager
            get() {
                if (configurationManager == null) {
                    configurationManager = ConfigurationManager()
                }
                return configurationManager as ConfigurationManager
            }
    }

}