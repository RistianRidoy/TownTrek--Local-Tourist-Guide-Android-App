package com.example.towntrek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.towntrek.PlanningScreenAdapter.FeaturedAdapter;
import com.example.towntrek.PlanningScreenAdapter.FeaturedHelperClass;

import java.util.ArrayList;

public class PlanningScreen extends AppCompatActivity /*implements FeaturedAdapter.OnItemClickListener*/{

    TextView textView;
    Animation animation;
    RecyclerView featuredRecycler, featuredRecycler02, featuredRecycler03;
    RecyclerView.Adapter adapter;
    ArrayList<FeaturedHelperClass> featuredLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_screen);

        textView = findViewById(R.id.planning_header);
        featuredRecycler = findViewById(R.id.featured_recycler);
        featuredRecycler02 = findViewById(R.id.featured_recycler02);
        featuredRecycler03 = findViewById(R.id.featured_recycler03);

        animation = AnimationUtils.loadAnimation(PlanningScreen.this, R.anim.animationbottom);
        textView.setAnimation(animation);
        textView.setVisibility(View.VISIBLE);
        
        featuredRecycler();
        featuredRecycler02();
        featuredRecycler03();
    }

    private void featuredRecycler() {
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredLocations = new ArrayList<>();
        featuredLocations.add(new FeaturedHelperClass(R.drawable.bandarban, "Bandarban, Bangladesh", "Bandarban, nestled in the Chittagong Hill Tracts, captivates with its lush landscapes and rich indigenous culture.", 22.1953 , 92.2195));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.coxs_bazar, "Cox's Bazar, Bangladesh", "World's longest natural sea beach, offering stunning coastal beauty.", 21.5833, 92.0167));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.jaflong, "Jaflong, Bangladesh", "Captivates with its breathtaking tea gardens and unique beauty of stone collection from the Dawki River bordering India.", 25.171614, 92.01833));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.shajek_valley, "Shajek, Bangladesh", "Where the hills and clouds intertwine, painting a breathtaking vista of a picture-perfect moment.",  23.381993, 92.293823));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.tanguar_haor, "Tanguar Haor, Bangladesh", "Explore its natural splendor through alluring boat rides and unforgettable stay-overs amidst the wetlands.", 25.0704, 91.3228));

        FeaturedAdapter featuredAdapter = new FeaturedAdapter(this, featuredLocations);
        featuredRecycler.setAdapter(featuredAdapter);
    }

    private void featuredRecycler02() {
        featuredRecycler02.setHasFixedSize(true);
        featuredRecycler02.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredLocations = new ArrayList<>();
        featuredLocations.add(new FeaturedHelperClass(R.drawable.santorini, "Santorini, Greece", "A whitewashed paradise crowned with blue domes, where sunsets paint the sky in breathtaking hues.", 36.393154, 25.461510));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.paris, "Paris, France", "The City of Light captivates with its romantic allure, adorned by iconic landmarks and timeless elegance",48.864716, 2.349014));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.kyoto, "Kyoto, Japan", "Kyoto's beauty whispers through its ancient temples, traditional gardens, and serene cherry blossom-strewn landscapes.",35.011665,135.768326));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.venice, "Venice, Italy", "Venice enchants with its ethereal waterways, intricate architecture, and a unique charm that dances on the edge of reality.",45.438759, 12.327145));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.prague, "Prague, Czech Republic", "Prague's beauty lies in its fairytale-like atmosphere, with a symphony of spires, cobbled streets, and historic bridges.", 50.073658, 14.418540));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.cape_town, "Cape Town, South Africa", "Cape Town's stunning blend of mountains, beaches, and urban vibrance creates a breathtaking backdrop against the sea.",-33.918861, 18.423300));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.rio_de_janeiro, "Rio de Janeiro, Brazil", "Rio's natural beauty mesmerizes with its lush rainforests, iconic Sugarloaf Mountain, and the vibrant energy of Copacabana Beach.", -22.908333, -43.196388));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.barcelona, "Barcelona, Spain", "Barcelona boasts Gaudi's architectural masterpieces, a Mediterranean coastline, and an artistic essence that colors every corner.",41.390205, 2.154007));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.sydney, "Sydney, Australia", "Sydney's beauty shines in its harmonious coexistence of modernity and nature, with the Opera House overlooking stunning harbors.",-33.865143, 151.209900));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.florence, "Florence, Italy", "Renaissance elegance thrives in Florence, where artistry graces every corner.",43.77925, 11.24626));

        FeaturedAdapter featuredAdapter = new FeaturedAdapter(this, featuredLocations);
        featuredRecycler02.setAdapter(featuredAdapter);
    }

    private void featuredRecycler03() {
        featuredRecycler03.setHasFixedSize(true);
        featuredRecycler03.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredLocations = new ArrayList<>();
        featuredLocations.add(new FeaturedHelperClass(R.drawable.gullfoss_waterfall, "Gullfoss Waterfall, Iceland", "Where the dance of Northern lights above cascading waterfalls creates a breathtaking symphony of beauty.", 64.3223, -20.1193));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.bali, "Bali, Indonesia", "A tropical paradise where lush landscapes, vibrant culture, and stunning beaches converge.", -8.409518, 115.188919));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.pattaya, "Pattaya, Thailand", "A vibrant coastal city known for its bustling nightlife, stunning beaches, and lively entertainment.", 12.927608, 100.877083));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.honshu_island, "Honshu Island, Japan", "Here stands Mount Fuji, a graceful mountain adorned by the graceful cherry blossom sakura trees.",36.0000, 138.0000));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.meeru_island, "Meeru Island, Maldives", "A pristine utopia where white sandy beaches meet crystal-clear turquoise waters.",4.453279, 73.716893));

        FeaturedAdapter featuredAdapter = new FeaturedAdapter(this, featuredLocations);
        featuredRecycler03.setAdapter(featuredAdapter);
    }
}
