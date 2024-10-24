# Minecraft2D

A 2D version of the popular java game: Minecraft. Built as part of a graded assignment.

## Description

The game is a 2D open world game with realistic graphics and procedural terrain generation. The terrain is split into chunks, which are 2D arrays of blocks. The player can move around and interact with the world by placing or destroying blocks, which impacts the lighting and is saved for when the game is launched again in the same world. The most advanced features of the game are its procedural terrain generation and lighting system, whose development process is described in detail below.

## The Lighting System

The lighting system works based on all objects within the game. Light sources present within the scene have are assigned coordinates and intensity values, which enables the game to draw shadows dynamically.

The solution relies on a technique I learned on [this page](https://www.redblobgames.com/articles/visibility/), which dives relativaly deeply into simple optimizations of ray casting algorithms. I learned that it is not necessary to cast rays and intersect them with every object but it suffices in the simple 2D environment to find the edges of objects and take only them into account. This approach significantly improved the efficiency of the program while also increasing the accuracy of the shadows.

In order to be able to use the technique described above, I needed a way to, rather than looping over each edge of every block in the world, to find edges of bigger rectangles. In order to optimize this algorithm even more, this is only done when the chunk is interacted with by the player.

During the development process I used sources such as [AWT docs](https://www.javatpoint.com/java-awt-panel) and the [Graphics2D documentation](https://docs.oracle.com/javase/8/docs/api/index.html?java/awt/Graphics2D.html) to find ways to shade the terrain. In order to handle lighting, I learned how to place a independent on top of another by
reading about JWT Panels [here](https://www.javatpoint.com/java-awt-panel). This way me and my colleague were not interfering when working on independent parts of the project, but utilizing the same terrain object.

My first approach of utilizing a radial gradient quickly ended up not working when more than one light source was applied and I had to find a different way to map the darkness and brightness of the environment. The second approach was far more accurate. It included a 2d array of pixels representing the screen. Every pixel was assigned a value (incremented) per light source in the frame. More factors were taken into account such as the distance from the light and its intensity.

# Demo Tiles Textures

https://piiixl.itch.io/textures
