package com.vk.id.onetap.compose.onetap

/**
 * Represents a scenario for which OneTap is used which affects OneTap's title.
 *
 * @since 2.1.0
 */
public enum class OneTapTitleScenario {

    /**
     * Default auth scenario.
     *
     * @since 2.1.0
     */
    SignIn,

    /**
     * For service sector and educational services.
     *
     * @since 2.1.0
     */
    SignUp,

    /**
     * For cases with a discount or bonus.
     *
     * @since 2.1.0
     */
    Get,

    /**
     * For the financial sector (account, card, deposit).
     *
     * @since 2.1.0
     */
    Open,

    /**
     * For the financial sector and complex products (project cost, mortgage).
     *
     * @since 2.1.0
     */
    Calculate,

    /**
     * For e-commerce carts with text "order as {user}".
     *
     * @since 2.1.0
     */
    Order,

    /**
     * For e-commerce carts with text "place order as {user}".
     *
     * @since 2.1.0
     */
    PlaceOrder,

    /**
     * For e-commerce and services where you need to submit a request for participation.
     *
     * @since 2.1.0
     */
    SendRequest,

    /**
     * For educational projects and participation in tenders.
     *
     * @since 2.1.0
     */
    Participate,
}
