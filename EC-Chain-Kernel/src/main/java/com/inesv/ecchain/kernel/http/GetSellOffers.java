package com.inesv.ecchain.kernel.http;


import com.inesv.ecchain.kernel.core.CoinSellOffer;
import com.inesv.ecchain.kernel.H2.H2Iterator;
import com.inesv.ecchain.kernel.H2.H2Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

public final class GetSellOffers extends APIRequestHandler {

    static final GetSellOffers instance = new GetSellOffers();

    private GetSellOffers() {
        super(new APITag[]{APITag.MS}, "currency", "account", "availableOnly", "firstIndex", "lastIndex");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws ParameterException {

        long currencyId = ParameterParser.getUnsignedLong(req, "currency", false);
        long accountId = ParameterParser.getAccountId(req, false);
        if (currencyId == 0 && accountId == 0) {
            return JSONResponses.MISSING_CURRENCY_ACCOUNT;
        }
        boolean availableOnly = "true".equalsIgnoreCase(req.getParameter("availableOnly"));

        int firstIndex = ParameterParser.getFirstIndex(req);
        int lastIndex = ParameterParser.getLastIndex(req);

        JSONObject response = new JSONObject();
        JSONArray offerData = new JSONArray();
        response.put("offers", offerData);

        H2Iterator<CoinSellOffer> offers = null;
        try {
            if (accountId == 0) {
                offers = CoinSellOffer.getCurrencyOffers(currencyId, availableOnly, firstIndex, lastIndex);
            } else if (currencyId == 0) {
                offers = CoinSellOffer.getAccountOffers(accountId, availableOnly, firstIndex, lastIndex);
            } else {
                CoinSellOffer offer = CoinSellOffer.getOffer(currencyId, accountId);
                if (offer != null) {
                    offerData.add(JSONData.offer(offer));
                }
                return response;
            }
            while (offers.hasNext()) {
                offerData.add(JSONData.offer(offers.next()));
            }
        } finally {
            H2Utils.h2close(offers);
        }

        return response;
    }

}
