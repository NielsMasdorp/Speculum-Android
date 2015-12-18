package com.nielsmasdorp.speculum.models;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class StockInformation {

    private String symbol;

    private String change;

    private String name;

    private String stockExchange;

    public StockInformation(String symbol, String change, String name, String stockExchange) {
        this.symbol = symbol;
        this.change = change;
        this.name = name;
        this.stockExchange = stockExchange;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getChange() {
        return change;
    }

    public String getName() {
        return name;
    }

    public String getStockExchange() {
        return stockExchange;
    }
}
