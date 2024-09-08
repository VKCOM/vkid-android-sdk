package com.vk.id.onetap.compose.onetap

/**
 * Represents a scenario for which OneTap is used which affects OneTap's title.
 */
public enum class OneTapTitleScenario {

    /**
     * Default auth scenario.
     */
    SignIn,

    /**
     * For service sector and educational services.
     */
    SignUp,

    /**
     * For cases with a discount or bonus.
     */
    Get,

    /**
     * For the financial sector (account, card, deposit).
     */
    Open,

    /**
     * For the financial sector and complex products (project cost, mortgage).
     */
    Calculate,

    /**
     * For e-commerce carts with text "order as {user}".
     */
    Order,

    /**
     * For e-commerce carts with text "place order as {user}".
     */
    PlaceOrder,

    /**
     * For e-commerce and services where you need to submit a request for participation.
     */
    SendRequest,

    /**
     * For educational projects and participation in tenders.
     */
    Participate,
}
