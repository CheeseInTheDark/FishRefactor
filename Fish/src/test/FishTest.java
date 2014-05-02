package test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.util.ArrayList;

import javambs.Direction;
import javambs.Environment;
import javambs.Fish;
import javambs.Location;
import junit.framework.TestCase;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FishTest extends TestCase
{
    @Mock
    private Environment environment;
    
    @Mock
    private Location location;
    
    @Mock
    private Color color;
    
    @Mock
    private Direction direction;
    
    private Fish fish;
    
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
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
        when(environment.objectAt(location)).thenReturn(fish);
        assertTrue(fish.isInEnv());
        
        when(environment.objectAt(location)).thenReturn(null);
        assertFalse(fish.isInEnv());
    }
    
    public void testFishCanGenerateDescriptionOfItself()
    {
        int fishId = fish.id();
        
        when(location.toString()).thenReturn("(0, 0)");
        when(direction.toString()).thenReturn("NORTH");
        
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
    
    public void testFishDoesNotReverseDirections()
    {
        mockMoveableEnvironment();
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
        when(new Boolean(environment.isEmpty((Location) any(Location.class)))).thenReturn(Boolean.TRUE);
     
    }
    
    private void mockNonMoveableEnvironment()
    {
        mockEnvironment();
        when(new Boolean(environment.isEmpty((Location) Matchers.any(Location.class)))).thenReturn(Boolean.FALSE);
    }
    
    private void mockEnvironment()
    {
        org.mockito.Mockito.when(direction.reverse()).thenReturn(Direction.NORTH);
        
        ArrayList neighbors = new ArrayList();
        neighbors.add(new Location(1, 0));
        neighbors.add(new Location(0, 1));
        when(environment.objectAt(location)).thenReturn(fish);
        when(environment.neighborsOf(location)).thenReturn(neighbors);
        
        when(environment.getDirection(location, new Location(0, 1))).thenReturn(Direction.SOUTH);
        when(environment.getNeighbor(location, Direction.NORTH)).thenReturn(new Location(0, -1));   
    }
}
