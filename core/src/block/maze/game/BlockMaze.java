package block.maze.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class BlockMaze extends ApplicationAdapter {
	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private Model box;
    private ModelInstance boxInstance;
    private ModelInstance boxInstance1;
    private ModelInstance boxInstance2;
	private Environment environment;
	private BitmapFont font;
	private SpriteBatch spriteBatch;
	
	@Override
	public void create () {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.vSyncEnabled = false; // Setting to false disables vertical sync
        config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
        config.backgroundFPS = 0; // Setting to 0 disables background fps throttling

        font = new BitmapFont();
        spriteBatch = new SpriteBatch();
		camera = new PerspectiveCamera(
				75,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		// Move the camera 3 units back along the z-axis and look at the origin
		camera.position.set(0f,0f,10f);
		camera.lookAt(0f,0f,0f);

		// Near and Far (plane) repesent the minimum and maximum ranges of the camera in, um, units
		camera.near = 0.1f;
		camera.far = 300.0f;

		// A ModelBatch is like a SpriteBatch, just for models.  Use it to batch up geometry for OpenGL
		modelBatch = new ModelBatch();

		box = makeBox();
		// A model holds all of the information about an, um, model, such as vertex data and texture info
		// However, you need an instance to actually render it.  The instance contains all the
		// positioning information ( and more ).  Remember Model==heavy ModelInstance==Light
        boxInstance = new ModelInstance(box,0,0,0);
        boxInstance1 = new ModelInstance(box,4,0,0);
        boxInstance2 = new ModelInstance(box,-4,0,0);

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

		// For some flavor, lets spin our camera around the Y axis by 1 degree each time render is called
//		camera.rotate(Vector3.Y,3f);
//		camera.translate(new Vector3(0,0,0.01f));
		// When you change the camera details, you need to call update();
		// Also note, you need to call update() at least once.
		camera.update();
		//boxInstance.transform.trn(0,0,0.1f);

		//Rotate the box
		boxInstance.transform.rotate(Vector3.X,1f);
        boxInstance1.transform.rotate(Vector3.Z,1f);
        boxInstance2.transform.rotate(Vector3.Y,1f);
		//boxInstance.calculateTransforms();

		// Like spriteBatch, just with models!  pass in the box Instance and the environment
		modelBatch.begin(camera);
        modelBatch.render(boxInstance, environment);
        modelBatch.render(boxInstance1, environment);
        modelBatch.render(boxInstance2, environment);
		modelBatch.end();

        spriteBatch.begin();
        font.draw(spriteBatch, "fps: " + Gdx.graphics.getFramesPerSecond(), 0, 20);
        spriteBatch.end();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
	}

	private Model makeBox() {
		Model box;
		// A ModelBuilder can be used to build meshes by hand
		ModelBuilder modelBuilder = new ModelBuilder();

		// It also has the handy ability to make certain premade shapes, like a Cube
		// We pass in a ColorAttribute, making our cubes diffuse ( aka, color ) red.
		// And let openGL know we are interested in the Position and Normal channels
//
        Material mat = new Material();
        modelBuilder.begin();
        MeshPartBuilder mp = modelBuilder.part("back", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked, mat);
            mp.setColor(Color.RED);
            mp.rect(-2f,-2f,-2f, -2f,2f,-2f,  2f,2f,-2f, 2f,-2f,-2f, 0,0,-1);
        mp = modelBuilder.part("front", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked, mat);
            mp.setColor(Color.GREEN);
            mp.rect(-2f,-2f,2f, 2f,-2f,2f,  2f,2f,2f, -2f,2f,2f, 0,0,1);
        mp = modelBuilder.part("left", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked, mat);
            mp.setColor(Color.BLUE);
            mp.rect(-2f,-2f,-2f, -2f,-2f,2f,  -2f,2f,2f, -2f,2f,-2f, -1,0,0);
        mp = modelBuilder.part("right", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked, mat);
            mp.setColor(Color.YELLOW);
            mp.rect(2f,-2f,2f, 2f,-2f,-2f,  2f,2f,-2f, 2f,2f,2f, 1,0,0);
        mp = modelBuilder.part("top", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked, mat);
            mp.setColor(Color.BLACK);
            mp.rect(-2f,2f,2f, 2f,2f,2f,  2f,2f,-2f, -2f,2f,-2f, 0,1,0);
        mp = modelBuilder.part("top", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked, mat);
            mp.setColor(Color.PURPLE);
            mp.rect(-2f,-2f,-2f, 2f,-2f,-2f,  2f,-2f,2f, -2f,-2f,2f, 0,-1,0);
        box = modelBuilder.end();

		return box;
	}
}
