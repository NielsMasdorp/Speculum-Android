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

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }
}
