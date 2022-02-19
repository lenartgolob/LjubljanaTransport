package com.kumuluzee;

import com.kumuluzee.LjubljanaTransportResponse.TaxiProvider;

import javax.enterprise.context.RequestScoped;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class Taxis {
    public static List<TaxiProvider> getTaxisProviders() {
        List<TaxiProvider> taxiProviders = new ArrayList<>();
        taxiProviders.add(new TaxiProvider("Avto Taxi Tima ekspres", "+386 (0)41 606 716", "taxi@amis.net"));
        taxiProviders.add(new TaxiProvider("BookTaxiSlovenija", "+386 (0)70 720 414", "info@taxi-airport-ljubljana.com"));
        taxiProviders.add(new TaxiProvider("Elit Taxi", "+386 (0)41 752 751", "elit@elit-taxi.si"));
        taxiProviders.add(new TaxiProvider("Gea Taxi", "+386 (0)1 433 34 44", "info@gea-taxi.biz "));
        taxiProviders.add(new TaxiProvider("Metro Taxi", "+ 386 (0)41 240", "info@taximetro.si"));
        taxiProviders.add(new TaxiProvider("Rumeni taxi", "+386 (0)41 731 831", "info@rumenitaxi.com"));
        taxiProviders.add(new TaxiProvider("Taxi društvo Ljubljana", "+386 (0)1 234 90 00", "info@taxi-ljubljana.si"));
        taxiProviders.add(new TaxiProvider("Taxi Intertours", "080 311 311", "info@taxi-intertours.si"));
        taxiProviders.add(new TaxiProvider("Taxi Laguna", "+386 (0)1 511 23 14", "taxi.laguna@siol.net"));
        taxiProviders.add(new TaxiProvider("Taxi Ljubljana Intereks", "+386 (0)41 460 460", "taxi@intereks.si"));
        taxiProviders.add(new TaxiProvider("Taxi Rondo", "080 900 900", "info@taxi-rondo.si"));
        taxiProviders.add(new TaxiProvider("Airtrail Slovenia", "+386 (0)41 281 228", "airtrail.slovenia@gmail.com"));
        taxiProviders.add(new TaxiProvider("Intaxi", "+386(0)31 40 20 20", "info@intaxi.si"));
        taxiProviders.add(new TaxiProvider("Taxi and Transfers TuamV", "+386 (0)40 805 761", "transfer@tuam.si"));

        return taxiProviders;
    }
}
