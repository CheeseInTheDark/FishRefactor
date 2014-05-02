package test;

import java.awt.Color;
import java.util.ArrayList;

import javambs.Direction;
import javambs.Fish;
import javambs.Location;
import junit.framework.TestCase;

import static org.mockito.Mockito.*;
import org.mockito.Matchers;

import javambs.Environment;

public class FishTest extends TestCase
{
    private Environment environment;
    private Location location;
    private Fish fish;
    private Color color;
    private Direction direction;
    
    public void setUp()
    {
        environment = (Environment) org.mockito.Mockito.mock(Environment.class);
        location = (Location) org.mockito.Mockito.mock(Location.class);
        color = (Color) org.mockito.Mockito.mock(Color.class);
        direction = (Direction) org.mockito.Mockito.mock(Direction.class);
        fish = new Fish(environment, location, direction, color);
    }
    
    public void testFishHasLocation()
    {
        assertEquals(location, fish.location());
    }
    
    public void testFishHasColor()
    {
        assertEquals(color, fish.color());
    }
 
    public void testFishReturnsEnvironment()
    {
        assertEquals(environment, fish.environment());
    }
    
    public void testFishKnowsIfItIsInTheEnvironment()
    {
        org.mockito.Mockito.when(environment.objectAt(location)).thenReturn(fish);
        assertTrue(fish.isInEnv());
        
        org.mockito.Mockito.when(environment.objectAt(location)).thenReturn(null);
        assertFalse(fish.isInEnv());
    }
    
    public void testFishCanGenerateDescriptionOfItself()
    {
        int fishId = fish.id();
        
        org.mockito.Mockito.when(location.toString()).thenReturn("(0, 0)");
        org.mockito.Mockito.when(direction.toString()).thenReturn("NORTH");
        
        String description = fishId + "(0, 0)NORTH";
        assertEquals(fish.toString(), description);
    }
    
    public void testEachFishHasAUniqueId()
    {
        int firstId = fish.id();
        
        int nextId = new Fish(environment, location).id();
        
        assertEquals(firstId + 1, nextId);
    }
    
    public void testFishMovesIfSpaceAvailable()
    {
        mockMoveableEnvironment();
        
        fish.act();

        assertFalse(new Location(0, 0).compareTo(fish.location()) == 0);
    }
    
    public void testFishDoesNotMoveIfSpaceNotAvailable()
    {
        mockNonMoveableEnvironment();
        
        fish.act();

        assertTrue(new Location(0, 0).compareTo(fish.location()) == 0);
    }
    
    private void mockMoveableEnvironment()
    {
        mockEnvironment();
        org.mockito.Mockito.when(new Boolean(environment.isEmpty((Location) Matchers.any(Location.class)))).thenReturn(Boolean.TRUE);
     
    }
    
    private void mockNonMoveableEnvironment()
    {
        mockEnvironment();
        org.mockito.Mockito.when(new Boolean(environment.isEmpty((Location) Matchers.any(Location.class)))).thenReturn(Boolean.FALSE);
    }
    
    private void mockEnvironment()
    {
        org.mockito.Mockito.when(direction.reverse()).thenReturn(Direction.NORTH);
        
        ArrayList neighbors = new ArrayList();
        neighbors.add(new Location(1, 0));
        neighbors.add(new Location(0, 1));
        org.mockito.Mockito.when(environment.objectAt(location)).thenReturn(fish);
        org.mockito.Mockito.when(environment.neighborsOf(location)).thenReturn(neighbors);
        
        org.mockito.Mockito.when(environment.getDirection(location, new Location(0, 1))).thenReturn(Direction.SOUTH);
        org.mockito.Mockito.when(environment.getNeighbor(location, Direction.NORTH)).thenReturn(new Location(0, -1));   
    }
}
