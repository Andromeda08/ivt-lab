package hu.bme.mit.spaceship;

import static junit.framework.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore torpedoStore1, torpedoStore2;

  @BeforeEach
  public void init(){
    this.ship = new GT4500();

    this.torpedoStore1 = mock(TorpedoStore.class);
    this.torpedoStore2 = mock(TorpedoStore.class);
    this.ship = new GT4500(torpedoStore1, torpedoStore2);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(torpedoStore1.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(torpedoStore1, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(torpedoStore1.fire(1)).thenReturn(true);
    when(torpedoStore2.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertTrue(result);
    verify(torpedoStore1, times(1)).fire(1);
    verify(torpedoStore2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Fail()
  {
    when(torpedoStore1.fire(1)).thenReturn(false);
    when(torpedoStore2.fire(1)).thenReturn(false);

    assertFalse(ship.fireTorpedo(FiringMode.ALL));

    verify(torpedoStore1, times(1)).fire(1);
    verify(torpedoStore2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Primary_Empty()
  {
    when(torpedoStore1.isEmpty()).thenReturn(true);
    when(torpedoStore1.fire(1)).thenReturn(false);

    when(torpedoStore2.fire(1)).thenReturn(true);
    when(torpedoStore2.fire(1)).thenReturn(true);

    assertTrue(ship.fireTorpedo(FiringMode.SINGLE));

    verify(torpedoStore2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Secondary_Empty()
  {
    when(torpedoStore1.fire(1)).thenReturn(true);
    when(torpedoStore2.fire(1)).thenReturn(false);

    var fire1 = ship.fireTorpedo(FiringMode.SINGLE);
    var fire2 = ship.fireTorpedo(FiringMode.SINGLE);

    assertTrue(fire1);
    assertFalse(fire2);

    verify(torpedoStore1, times(1)).fire(1);
    verify(torpedoStore2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Primary_Repeat_Secondary_Empty()
  {
    when(torpedoStore1.isEmpty()).thenReturn(false);
    when(torpedoStore2.isEmpty()).thenReturn(true);

    when(torpedoStore1.fire(1)).thenReturn(true);
    when(torpedoStore2.fire(1)).thenReturn(true);

    for (var i = 0; i < 2; i++) {
      var result = ship.fireTorpedo(FiringMode.SINGLE);
      assertTrue(result);
    }

    verify(torpedoStore1, times(2)).fire(1);
  }

}