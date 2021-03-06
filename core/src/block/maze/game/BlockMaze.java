package block.maze.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
//import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import static com.badlogic.gdx.graphics.GL20.*;

public class BlockMaze extends ApplicationAdapter {
    static final int MAX_BLOCKS = 2048;

	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private Model box;
	private Environment environment;
	private BitmapFont font;
	private SpriteBatch spriteBatch;
    private CameraInputController camController;
    private Texture tex;
    private TextureRegion[][] tile;
    private ModelInstance[] blocks = new ModelInstance[MAX_BLOCKS];
    private ModelInstance chunks;
    private Material mat;
	
	@Override
	public void create () {

//        Gdx.graphics.setWindowedMode(960,540);

        Gdx.app.log("KRU", "renderer: " + Gdx.gl.glGetString(GL20.GL_RENDERER));
        Gdx.app.log("KRU", "vendor: " + Gdx.gl.glGetString(GL20.GL_VENDOR));
        Gdx.app.log("KRU", "version: " + Gdx.gl.glGetString(GL20.GL_VERSION));

        tex = new Texture("terrain.png");
        tile = TextureRegion.split(tex, 16, 16);
        font = new BitmapFont();
        spriteBatch = new SpriteBatch();
		camera = new PerspectiveCamera(
				67,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		// Move the camera 3 units back along the z-axis and look at the origin
		camera.position.set(0f,0f,0f);
		camera.lookAt(0f,0f,0f);

		// Near and Far (plane) represents the minimum and maximum ranges of the camera in, um, units
		camera.near = 0.1f;
		camera.far = 300.0f;

        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);

		// A ModelBatch is like a SpriteBatch, just for models.  Use it to batch up geometry for OpenGL
		modelBatch = new ModelBatch();

        mat = new Material(); //new Material(TextureAttribute.createDiffuse(tex));
        //mat.set(IntAttribute.createCullFace(GL_NONE));
        //mat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 1f));

		// A model holds all of the information about an, um, model, such as vertex data and texture info
		// However, you need an instance to actually render it.  The instance contains all the
		// positioning information ( and more ).  Remember Model==heavy ModelInstance==Light
//        for (int i=0; i<MAX_BLOCKS; i++) {
//            int x = (int)(Math.random() * 64f)*4-128;
//            int y = (int)(Math.random() * 64f)*4-128;
//            int z = (int)(Math.random() * 64f)*4-128;
//            box = makeBox(x,y,z);
//            blocks[i] = new ModelInstance(box,x, y, z);
//        }

        Model chunk = makeRandomBoxes();
        chunks = new ModelInstance(chunk);

		// Finally we want some light, or we wont see our color.  The environment gets passed in during
		// the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));


    }

	@Override
	public void render () {
		// You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT when working in 3D
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0.7f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 		float delta = Gdx.graphics.getDeltaTime();
		float speed = delta * 100.0f;

		// When you change the camera details, you need to call update();
		// Also note, you need to call update() at least once.
        camController.update();
		camera.update();

		// Like spriteBatch, just with models!  pass in the box Instances and the environment
		modelBatch.begin(camera);
//		for (int i=0; i<MAX_BLOCKS; i++) {
//            blocks[i].transform.rotate(Vector3.X,(i%3-1)*speed);
//            blocks[i].transform.rotate(Vector3.Z,((i/3)%3-1)*speed);
//            blocks[i].transform.rotate(Vector3.Y,((i/9)%3-1)*speed);
//            modelBatch.render(blocks[i], environment);
//        }
//        chunks.transform.rotate(Vector3.Y,speed*0.1f);
        modelBatch.render(chunks,environment);
		modelBatch.end();

        spriteBatch.begin();
        font.draw(spriteBatch, "delta: " + Gdx.graphics.getDeltaTime()*100f, 0, 20);
        font.draw(spriteBatch, "fps: " + Gdx.graphics.getFramesPerSecond(), 0, 40);
        spriteBatch.end();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		spriteBatch.dispose();

	}

	private Model makeRandomBoxes() {
	    Model chunk;
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshPartBuilder mp;

        modelBuilder.begin();

        for(int i=0; i<2048;i++) {

            int x = (int)(Math.random() * 64f)*4-128;
            int y = (int)(Math.random() * 64f)*4-128;
            int z = (int)(Math.random() * 64f)*4-128;

            Color color1 = new Color((x+128)/256f,(y+128)/256f,(z+128)/256f,1);
            Color color2 = new Color((x+128+8)/256f,(y+128+8)/256f,(z+128+8)/256f,1);
            Color color3 = new Color((x+128-8)/256f,(y+128-8)/256f,(z+128-8)/256f,1);

            mp = modelBuilder.part("back", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
            mp.setColor(color1);
            mp.setUVRange(tile[0][6]);
            mp.rect(x-2f, y-2f, z-2f, x-2f, y+2f, z-2f, x+2f, y+2f, z-2f, x+2f, y-2f, z-2f, 0, 0, -1);
            mp = modelBuilder.part("front", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
            mp.setColor(color1);
            mp.setUVRange(tile[0][6]);
            mp.rect(x-2f, y-2f, z+2f, x+2f, y-2f, z+2f, x+2f, y+2f, z+2f, x-2f, y+2f, z+2f, 0, 0, 1);
            mp = modelBuilder.part("left", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
            mp.setColor(color2);
            mp.setUVRange(tile[0][6]);
            mp.rect(x-2f, y-2f, z-2f, x-2f, y-2f, z+2f, x-2f, y+2f, z+2f, x-2f, y+2f, z-2f, -1, 0, 0);
            mp = modelBuilder.part("right", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
            mp.setColor(color2);
            mp.setUVRange(tile[0][6]);
            mp.rect(x+2f, y-2f, z+2f, x+2f, y-2f, z-2f, x+2f, y+2f, z-2f, x+2f, y+2f, z+2f, 1, 0, 0);
            mp = modelBuilder.part("top", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
            mp.setColor(color3);
            mp.setUVRange(tile[0][6]);
            mp.rect(x-2f, y+2f, z+2f, x+2f, y+2f, z+2f, x+2f, y+2f, z-2f, x-2f, y+2f, z-2f, 0, 1, 0);
            mp = modelBuilder.part("bot", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
            mp.setColor(color3);
            mp.setUVRange(tile[0][6]);
            mp.rect(x-2f, y-2f, z-2f, x+2f, y-2f, z-2f, x+2f, y-2f, z+2f, x-2f, y-2f, z+2f, 0, -1, 0);
        }
        chunk = modelBuilder.end();
        return chunk;
    }

	private Model makeBox(int x, int y, int z) {
		Model box;
		// A ModelBuilder can be used to build meshes by hand
		ModelBuilder modelBuilder = new ModelBuilder();

		// It also has the handy ability to make certain premade shapes, like a Cube
		// We pass in a ColorAttribute, making our cubes diffuse ( aka, color ) red.
		// And let openGL know we are interested in the Position and Normal channels

        Color color1 = new Color((x+128)/256f,(y+128)/256f,(z+128)/256f,1f);
        Color color2 = new Color((x+128+8)/256f,(y+128+8)/256f,(z+128+8)/256f,1f);
        Color color3 = new Color((x+128-8)/256f,(y+128-8)/256f,(z+128-8)/256f,1f);
        modelBuilder.begin();
        MeshPartBuilder mp;
        mp = modelBuilder.part("back", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
            mp.setColor(color1);
            mp.setUVRange(tile[1][1]);
            mp.rect(-2f,-2f,-2f, -2f,2f,-2f,  2f,2f,-2f, 2f,-2f,-2f, 0,0,-1);
        mp = modelBuilder.part("front", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
        mp.setColor(color1);
            mp.setUVRange(tile[0][1]);
            mp.rect(-2f,-2f,2f, 2f,-2f,2f,  2f,2f,2f, -2f,2f,2f, 0,0,1);
        mp = modelBuilder.part("left", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
        mp.setColor(color2);
            mp.setUVRange(tile[0][3]);
            mp.rect(-2f,-2f,-2f, -2f,-2f,2f,  -2f,2f,2f, -2f,2f,-2f, -1,0,0);
        mp = modelBuilder.part("right", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
        mp.setColor(color2);
            mp.setUVRange(tile[0][12]);
            mp.rect(2f,-2f,2f, 2f,-2f,-2f,  2f,2f,-2f, 2f,2f,2f, 1,0,0);
        mp = modelBuilder.part("top", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
        mp.setColor(color3);
            mp.setUVRange(tile[0][2]);
            mp.rect(-2f,2f,2f, 2f,2f,2f,  2f,2f,-2f, -2f,2f,-2f, 0,1,0);
        mp = modelBuilder.part("bot", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked | Usage.TextureCoordinates, mat);
        mp.setColor(color3);
            mp.setUVRange(tile[1][0]);
            mp.rect(-2f,-2f,-2f, 2f,-2f,-2f,  2f,-2f,2f, -2f,-2f,2f, 0,-1,0);
        box = modelBuilder.end();

		return box;
	}
}
