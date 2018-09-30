package com.savory.api.clients.savory.mock;

import com.savory.api.clients.savory.mock.models.MockDishItem;
import com.savory.api.clients.savory.mock.models.MockRestaurant;
import com.savory.api.clients.savory.mock.models.MockUser;

import java.util.ArrayList;
import java.util.List;

public class MockDataProvider {

    private List<MockDishItem> dishItems;

    public MockDataProvider() {
        dishItems = new ArrayList<>();

        MockUser tonyHawk = new MockUser()
                .setName("Tony Hawk")
                .setProfilePictureUrl("https://upload.wikimedia.org/wikipedia/commons/" +
                        "thumb/3/36/Skater_Tony_Hawk.jpg/240px-Skater_Tony_Hawk.jpg");
        MockRestaurant sweetGarden = new MockRestaurant()
                .setName("Sweet Garden")
                .setAddress("39473 Fremont Blvd, Fremont, CA 94538");
        MockDishItem crispyChicken = new MockDishItem()
                .setDishId(1)
                .setName("Crispy Chicken")
                .setNumLikes(10)
                .setPhotoUrl("https://s3-media1.fl.yelpcdn.com/bphoto/CUC8Mew3HP8Yj6Qpgtmfww/o.jpg")
                .setDescription("Great texture and perfectly seasoned! " +
                        "Definitely some of the best fried chicken I've ever had.")
                .setRating(5)
                .setUser(tonyHawk)
                .setRestaurant(sweetGarden);
        dishItems.add(crispyChicken);

        MockUser elonMusk = new MockUser()
                .setName("Elon Musk")
                .setProfilePictureUrl("https://media.apnarm.net.au/media/images/2018/03/24/" +
                        "imagev1e0b05bc58eeae26ec94f0f8a97157e81-5j6d1a7kr9fn8htbzp2_ct677x380.jpg");
        MockRestaurant kayumiDiner = new MockRestaurant()
                .setName("KA YuMi Diner")
                .setAddress("40645 Fremont Blvd, Ste 8, Fremont, CA 94538");
        MockDishItem tofuSoup = new MockDishItem()
                .setDishId(2)
                .setName("Seafood Spicy Tofu Soup")
                .setNumLikes(3)
                .setPhotoUrl("https://s3-media3.fl.yelpcdn.com/bphoto/uaj3Wj3aSYfTIy4lxmAOOg/o.jpg")
                .setDescription("Waaay too many mushrooms in this")
                .setRating(2)
                .setUser(elonMusk)
                .setRestaurant(kayumiDiner);
        dishItems.add(tofuSoup);

        MockUser donaldGlover = new MockUser()
                .setName("Donald Glover")
                .setProfilePictureUrl("https://wrhsstampede.com/wp-content/uploads/2017/05/IMG_5981.jpg");
        MockRestaurant salaThai = new MockRestaurant()
                .setName("Sala Thai")
                .setAddress("3241 Walnut Ave, Fremont, CA 94538 ");
        MockDishItem rotiCanai = new MockDishItem()
                .setDishId(3)
                .setName("Roti Canai")
                .setNumLikes(157)
                .setPhotoUrl("https://s3-media3.fl.yelpcdn.com/bphoto/bIGXzFqkoMIXr2wSGXGtWQ/o.jpg")
                .setDescription("The dipping sauce was great!")
                .setRating(4)
                .setUser(donaldGlover)
                .setRestaurant(salaThai);
        dishItems.add(rotiCanai);
    }

    public List<MockDishItem> getMockDishItems() {
        return new ArrayList<>(dishItems);
    }
}
