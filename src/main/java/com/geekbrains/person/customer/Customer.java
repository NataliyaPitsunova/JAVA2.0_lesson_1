package com.geekbrains.person.customer;

import com.geekbrains.market.Market;
import com.geekbrains.person.Person;
import com.geekbrains.person.seller.Seller;
import com.geekbrains.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Customer extends Person {
    private List<Product> expectedPurchaseList;
    private List<Product> purchaseList;

    public Customer(List<Product> expectedPurchaseList, int cash) {
        this.purchaseList = new ArrayList<>();
        this.expectedPurchaseList = expectedPurchaseList;
        this.setCash(cash);
    }

    public void addPurchase(Product product) {
        if (purchaseList == null) {
            purchaseList = new ArrayList<>();
        }
        purchaseList.add(product);
    }

    public void findSellerOnMarket(Market market, String findName, String findLastName) {
        //сортируем продавцов, чтобы нужный был первый в списке
        boolean sellerOn = false;  //переменная чтобы понять есть ли вообще такой продавец, чтобы выдавать сканнер в случае отсутствия
        List<Seller> sellers = market.getSellers();

        for (int i = 0; i < sellers.size(); i++) {
            Seller sellersI = market.getSellers().get(i);
            if (sellersI.getName().equals(findName) && sellersI.getLastName().equals(findLastName)) {
                sellers.add(sellers.get(0));
                sellers.set(0, sellersI);
                sellers.set(i, sellers.get(sellers.size() - 1));
                sellers.remove(sellers.size() - 1);
                sellerOn = true;
                break;
            } else
                continue;
        }
        if (!(sellerOn)) {
            System.out.println("Продавца с таким именем нет, продолжить покупки у других продацов? Напишете Y - да");
            Scanner scanner = new Scanner(System.in);
            if (scanner.nextLine() == "Y") {
                findProductOnMarket(market);
            }
        } else {
            findProductOnMarket(market);
        }
    }

    public void findProductOnMarket(Market market) {
        for (int j = 0; j < expectedPurchaseList.size(); j++) {            //продукты
            for (int i = 0; i < market.getSellers().size(); i++) {         //продавцы, начиная с нужного, если такой есть
                boolean isBought = market.getSellers().get(i).sellProducts(this, expectedPurchaseList.get(j));
                if (isBought) {
                    //если есть выходим и переходим к сл.продукту
                    break;
                } else {
                    //если нет продукта идем к следущему продавцу
                    continue;
                }
            }
        }
    }

    public void info() {
        StringBuilder result = new StringBuilder("Я купил ");
        if (purchaseList.size() == 0) {
            result.append("ничего");
        } else {
            for (Product product : purchaseList) {
                result.append(product.getName());
                result.append(" в количестве ");
                result.append(product.getQuantity());
                result.append(" ");
            }
        }

        result.append(". У меня осталось: ");
        result.append(getCash());
        result.append(" рублей");

        System.out.println(result);
    }

    public List<Product> getExpectedPurchaseList() {
        return expectedPurchaseList;
    }

    public List<Product> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<Product> purchaseList) {
        this.purchaseList = purchaseList;
    }

}
