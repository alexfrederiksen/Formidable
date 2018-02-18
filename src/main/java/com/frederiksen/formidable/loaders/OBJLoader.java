package com.frederiksen.formidable.loaders;

import com.frederiksen.formidable.rendering.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OBJLoader {
    public static Mesh importModel(String filename)  {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(filename));
        } catch (IOException e) {
            System.err.println("Failed to load model: " + filename);
            return null;
        }
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> texCoords = new ArrayList<>();
        List<Vector3i> indices = new ArrayList<>();

        while (scanner.hasNext()) {
            String token = scanner.next();
            // skip comment lines
            if (token.equals("#")) scanner.nextLine();
            // read token
            switch(token) {
                case "v":
                    vertices.add(new Vector3f(
                            scanner.nextFloat(),
                            scanner.nextFloat(),
                            scanner.nextFloat()));
                    break;
                case "vn":
                    normals.add(new Vector3f(
                            scanner.nextFloat(),
                            scanner.nextFloat(),
                            scanner.nextFloat()));
                    break;
                case "vt":
                    texCoords.add(new Vector2f(
                            scanner.nextFloat(),
                            scanner.nextFloat()));
                    break;

                case "f":
                    indices.add(parseIndex(scanner.next()));
                    indices.add(parseIndex(scanner.next()));
                    indices.add(parseIndex(scanner.next()));
                    break;
            }
        }
        // use vertex part of indices as final indices
        int[] sortedIndices = new int[indices.size()];
        for (int i = 0; i < sortedIndices.length; i++) {
            sortedIndices[i] = indices.get(i).x;
        }
        // expand vertices into float array
        float[] sortedVertices = new float[vertices.size() * 3];
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vec = vertices.get(i);
            sortedVertices[i * 3] = vec.x;
            sortedVertices[i * 3 + 1] = vec.y;
            sortedVertices[i * 3 + 2] = vec.z;
        }

        // create sorted tex coords and normals arrays
        float[] sortedTexCoords = new float[vertices.size() * 2];
        float[] sortedNormals = new float[vertices.size() * 3];
        // set arrays by indices
        for (Vector3i index : indices) {
            if (index.y >= 0) {
                sortedTexCoords[index.x * 2] = texCoords.get(index.y).x;
                sortedTexCoords[index.x * 2 + 1] = texCoords.get(index.y).y;
            }
            if (index.z >= 0) {
                sortedNormals[index.x * 3] = normals.get(index.z).x;
                sortedNormals[index.x * 3 + 1] = normals.get(index.z).y;
                sortedNormals[index.x * 3 + 2] = normals.get(index.z).z;
            }
        }

        return new Mesh(sortedVertices, sortedTexCoords, sortedNormals, sortedIndices);
    }

    /**
     * Parse things in "token1/token2/token3" format
     * return -1 values for ones that are empty like "1//2"
     * @param index
     * @return
     */
    private static Vector3i parseIndex(String index) {
        int slash1 = index.indexOf("/");
        int slash2 = index.indexOf("/", slash1 + 1);

        String token1 = index.substring(0, slash1);
        String token2 = index.substring(slash1 + 1, slash2);
        String token3 = index.substring(slash2 + 1);

        return new Vector3i(
                token1.isEmpty() ? -1 : Integer.parseInt(token1) - 1,
                token2.isEmpty() ? -1 : Integer.parseInt(token2) - 1,
                token3.isEmpty() ? -1 : Integer.parseInt(token3) - 1);
    }
}
