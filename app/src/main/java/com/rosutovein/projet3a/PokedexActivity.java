package com.rosutovein.projet3a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.http.GET;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokedexActivity extends AppCompatActivity{

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private List<Pokemon>pokemonList;
    private List<PokemonInformations> pokemonInformationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Le contenu de la vue activity_main.xml sera celui sur lequels sera appliqué les prochaines lignes
        setContentView(R.layout.activity_pokedex);

        //Ajouter la toolbar sur l'activité
        Toolbar pokedexToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(pokedexToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Créer un objet qui permet de garder en mémoire les données (utiles pour ne pas avoir une application vide par manque de connexion)
        sharedPreferences = getSharedPreferences(Constants.APPLICATION_NAME, Context.MODE_PRIVATE);
        gson = new GsonBuilder().setLenient().create();

        pokemonList = getDataFromCache();
        if(pokemonList != null && !haveInternetConnection()){
            showList(pokemonList);
        }
        else{
            makeApiCall();
        }
    }

    private boolean haveInternetConnection(){

        NetworkInfo network = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if(network == null || !network.isConnected()) {
            return false;
        }
        return true;
    }

    //Permet de charger les données depuis le cache
    private List<Pokemon> getDataFromCache(){

        String jsonPokemon = sharedPreferences.getString(Constants.KEY_POKEMON_LIST, null);

        if(jsonPokemon == null){
            return null;
        }
        else{
            Type listType = new TypeToken<List<Pokemon>>(){}.getType();
            return gson.fromJson(jsonPokemon,listType);
        }
    }




    /*Afficher la recycleView*/
    private void showList(List<Pokemon> pokemonList){

        //On récupère la recycleView du fichier activity_main
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //Option permettant d'améliorer les performances
        recyclerView.setHasFixedSize(true);
        //On choisit un disposition linéaire pour notre recycleView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Défini un adapter
        ListAdapter myAdapter = new ListAdapter(pokemonList);
        recyclerView.setAdapter(myAdapter);
    }




    /*Appeler l'API pokeapi*/
    //int token = 1;
    private static final String BASE_URL = "https://pokeapi.co/";
    private static final String SPRITES_BASE_URL = "https://www.pokebip.com/pokedex/images/sugimori/";
    private void makeApiCall(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PokeApi pokeApi = retrofit.create(PokeApi.class);
        PokeApi pokeApi2 = retrofit.create(PokeApi.class);

        Call<RestPokemonResponse> call = pokeApi.getPokemonResponse();

        //On créer un callback qui va "diagnostiquer" la réponse du serveur.
        //On créer deux méthode, une en cas de réponse du serveur et ...
        //... une en cas d'absence de réponse.
        call.enqueue(new Callback<RestPokemonResponse>() {
            @Override
            public void onResponse(@NonNull Call<RestPokemonResponse> call, @NonNull Response<RestPokemonResponse> response) {

                if(response.isSuccessful() && response.body() != null){
                    pokemonList = response.body().getResults();

                    int currentPokemon = 0;
                    for(int i = 1; i < pokemonList.size()+1; i++){
                        pokemonList.get(currentPokemon).setId(i);
                        pokemonList.get(currentPokemon).setSprite(SPRITES_BASE_URL+ i + ".png");
                        currentPokemon++;
                    }
                    saveList(pokemonList);
                    showList(pokemonList);
                }
                else{
                    showError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestPokemonResponse> call, @NonNull Throwable t) {
                showError();
            }
        });


        //for(int i = 1; i < 806; i++) {
        Call<RestPokemonInformations>call2 = pokeApi2.getPokemonInformationsResponse(1);
            call2.enqueue(new Callback<RestPokemonInformations>() {
                @Override
                public void onResponse(@NonNull Call<RestPokemonInformations> call2, @NonNull Response<RestPokemonInformations> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        /*On récupère le poids, la taille, les deux types*/
                        Integer newWeight = response.body().getWeight();
                        Integer newHeight  = response.body().getHeight();
                        pokemonInformationsList = response.body().getInformationsResults();

                        /*On les associe au pokemon correspondant*/
                        pokemonList.get(0).setPokemonInformations(pokemonInformationsList);
                        pokemonList.get(0).setHeight(newHeight);
                        pokemonList.get(0).setWeight(newWeight);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RestPokemonInformations> call2, @NonNull Throwable t) {
                    Log.i("Fail", "it doesn't works working!");
                }
            });
        //}
    }

    private void saveList(List<Pokemon> pokemonList) {
        String jsonString = gson.toJson(pokemonList);

        sharedPreferences.edit()
                .putString(Constants.KEY_POKEMON_LIST, jsonString)
                .apply();
        Toast.makeText(getApplicationContext(), "List saved!", Toast.LENGTH_SHORT).show();
    }
    private void showError(){

        Toast.makeText(getApplicationContext(), "API Error!", Toast.LENGTH_SHORT).show();
    }
}
