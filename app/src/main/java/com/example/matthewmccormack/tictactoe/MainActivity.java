package com.example.matthewmccormack.tictactoe;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.util.Log;
import android.widget.Toast;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private Button mButton1;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Handler handler;
    private ThreadPoolExecutor executor;
    private LinkedBlockingQueue<Runnable> mWorkQueue;


    private boolean isOpen = false;
    private int[] gameSpaces = {0,0,0,0,0,0,0,0,0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton1 = (Button) findViewById(R.id.button1);
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData(0,1,2));
        gridView.setAdapter(gridAdapter);




        mButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int count = 0;
                int player = 1;

                //resets game
                for (int i=0;i<9;++i){
                    gameSpaces[i] = 0;
                }


                //main game loop
                while (count < 9) {
                    if (player == 1) {
                        playGame(player);
                        //run thread
                        //executor.execute(player1);
                        player = 2;
                        count++;

                        int result = gameStatus();
                        if (result == 1){
                            count = 9;
                            Toast.makeText(MainActivity.this, "Player 1 Wins!", Toast.LENGTH_SHORT).show() ;

                        }
                        else if (count == 9){
                            Toast.makeText(MainActivity.this, "Draw!", Toast.LENGTH_SHORT).show() ;
                        }
                    }
                    else if (player == 2){
                        playGame(player);
                        //executor.execute(player2);
                        player = 1;
                        count++;

                        int result = gameStatus();
                        if (result == 2){
                            count = 9;
                            Toast.makeText(MainActivity.this, "Player 2 Wins!", Toast.LENGTH_SHORT).show() ;

                        }
                        else if (count == 9){
                            Toast.makeText(MainActivity.this, "Draw!", Toast.LENGTH_SHORT).show() ;
                        }
                    }
                    //update UI
                    //executor.execute(r2);
                }
                Log.v("Game Status","Ended");
                displayPicture();

            }
        });

    }

    public void playGame (final int player){

        Runnable r3 = new Runnable() {
            @Override
            public void run() {
                int spaceNumber = 0;
                Log.v ("playGame", "Inside of playGame");
                while (!isOpen){
                    Random r = new Random();
                    spaceNumber = r.nextInt(9);

                    //if space is open set isOpen to true and place marker
                    if (gameSpaces[spaceNumber] == 0){
                        Log.v("GameSpacesBefore", String.valueOf(gameSpaces[spaceNumber]));
                        //break loop
                        isOpen = true;
                        //update gameSpace array TO DO: will need to add determination of which player filled the space 1 or 2
                        gameSpaces[spaceNumber] = player;
                        Log.v("RandomNum", String.valueOf(spaceNumber));
                        Log.v("GameSpacesAfter", String.valueOf(gameSpaces[spaceNumber]));
                    }
                }
                //reset boolean
                isOpen = false;
            }
        };
        Thread t1 = new Thread(r3);
        t1.start();

        displayPicture();

    }


    public void displayPicture() {

        //  The display of the picture must be executed on the UI thread
        //  Must call runOnUiThread() to make this happen
        runOnUiThread(new Runnable(){
            public void run() {
                Log.v("Location", "In runOnUiThread Call");


                gridAdapter.updateItemList(getData(0,1,2));


            }
        });
    }


    //check game status
    public int gameStatus (){
        // PLAYER 1 WINS
        if (gameSpaces[0]==1 && gameSpaces[1] ==1 && gameSpaces[2]==1){
            //player 1 wins across top
            return 1;
        }
        else if (gameSpaces[3]==1 && gameSpaces[4]==1 && gameSpaces[5]==1){
            //player 1 wins across middle
            return 1;
        }
        else if (gameSpaces[6]==1 && gameSpaces[7]==1 && gameSpaces[8]==1){
            //player 1 wins across bottom
            return 1;
        }
        else if (gameSpaces[0]==1 && gameSpaces[3]==1 && gameSpaces[6]==1){
            //player 1 wins down left side
            return 1;
        }
        else if (gameSpaces[1]==1 && gameSpaces[4]==1 && gameSpaces[7]==1){
            //player 1 wins down middle
            return 1;
        }
        else if (gameSpaces[2]==1 && gameSpaces[5]==1 && gameSpaces[8]==1){
            //player 1 wins down right side
            return 1;
        }
        else if (gameSpaces[0]==1 && gameSpaces[4]==1 && gameSpaces[8]==1){
            //player 1 wins diagonal top left to bottom right
            return 1;
        }
        else if (gameSpaces[2]==1 && gameSpaces[4]==1 && gameSpaces[6]==1){
            //player 1 wins diagonal top right to bottom left
            return 1;
        }
        // PLAYER 2 WINS
        else if (gameSpaces[0]==2 && gameSpaces[1] ==2 && gameSpaces[2]==2){
            //player 2 wins across top
            return 1;
        }
        else if (gameSpaces[3]==2 && gameSpaces[4]==2 && gameSpaces[5]==2){
            //player 2 wins across middle
            return 1;
        }
        else if (gameSpaces[6]==2 && gameSpaces[7]==2 && gameSpaces[8]==2){
            //player 2 wins across bottom
            return 2;
        }
        else if (gameSpaces[0]==2 && gameSpaces[3]==2 && gameSpaces[6]==2){
            //player 2 wins down left side
            return 2;
        }
        else if (gameSpaces[1]==2 && gameSpaces[4]==2 && gameSpaces[7]==2){
            //player 2 wins down middle
            return 2;
        }
        else if (gameSpaces[2]==2 && gameSpaces[5]==2 && gameSpaces[8]==2){
            //player 2 wins down right side
            return 2;
        }
        else if (gameSpaces[0]==2 && gameSpaces[4]==2 && gameSpaces[8]==2){
            //player 2 wins diagonal top left to bottom right
            return 2;
        }
        else if (gameSpaces[2]==2 && gameSpaces[4]==2 && gameSpaces[6]==2){
            //player 2 wins diagonal top right to bottom left
            return 2;
        }
        else {
            return 0;
        }

    }
    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData(int blank, int player1, int player2) {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        //Log.v("getData", "Inside getData");
        for (int i = 0; i < 9; i++) {
            //case when space is open
            if (gameSpaces[i] == 0){
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(blank, -1));
                imageItems.add(new ImageItem(bitmap, "Image#" + i));
            }
            //case when player 1 - letter o occupies space
            else if (gameSpaces[i] == 1){
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(player1, -1));
                imageItems.add(new ImageItem(bitmap, "Image#" + i));
            }
            //case when player 2 - letter x occupies space
            else if (gameSpaces[i] == 2){
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(player2, -1));
                imageItems.add(new ImageItem(bitmap, "Image#" + i));
            }

        }
        return imageItems;
    }
}