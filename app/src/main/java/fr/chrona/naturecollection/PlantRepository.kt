package fr.chrona.naturecollection

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.chrona.naturecollection.PlantRepository.Singleton.databaseRef
import fr.chrona.naturecollection.PlantRepository.Singleton.downloadUri
import fr.chrona.naturecollection.PlantRepository.Singleton.plantList
import fr.chrona.naturecollection.PlantRepository.Singleton.storageReference
import java.util.*


class PlantRepository {

    object Singleton {

        // donner le lien pour acceder au bucket
        private const val BUCKET_URL: String = "gs://nature-collection-5a6d5.appspot.com"

        // se connecter à notre espace de stokage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter à la reference "plant"
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // créer une liste qui va contenir nos plantes
        val plantList = arrayListOf<PlantModel>()

        //contenir le lien de l'image courante
        var downloadUri: Uri? = null
    }

    fun updateData(callback: () -> Unit) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les anciennes
                plantList.clear()
                // recolter les liste
                for (ds in snapshot.children) {
                    //construire un objet plante
                    val plant = ds.getValue(PlantModel::class.java)
                    if (plant != null) {
                        // ajouter le plante à notre liste
                        plantList.add(plant)
                    }
                }
                // actionner le callback
                callback()

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // créer une fonction pour envoyer des fichier sur le storage
    fun uploadImage(file: Uri, callback: () -> Unit) {
        // verifier que ce fichier n'est pas null
        if (file != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

            // demarrer la tache d'envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->

                // si il y a eu un probleme lors de l'envoi du fichier
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }

                return@Continuation ref.downloadUrl

            }).addOnCompleteListener { tack ->
                // verifier si tout a bien fonctionné
                if (tack.isSuccessful) {
                    // recuperer l'image
                    downloadUri = tack.result
                    callback()
                }
            }
        }
        }

        // mettre à jour un objet plante en objet
        fun updatePlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

        // inserer une nouvelle palnte en bdd
        fun insertPlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

        // supprimer une plante de la base
        fun deletePlant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()
    }




