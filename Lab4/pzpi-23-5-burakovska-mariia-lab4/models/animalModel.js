try {
    const knexConfig = require('../knexfile');
    const knex = require('knex')(knexConfig.development);
    
    const redis = require('redis');
    const redisClient = redis.createClient({ 
        url: process.env.REDIS_URL || 'redis://localhost:6379' 
    });
   
    redisClient.connect()
        .then(() => console.log("DEBUG: Redis connected successfully."))
        .catch(err => console.error("Redis connection error:", err));

    async function clearCache() {
        try {
            await redisClient.del('all_animals');
            console.log("DEBUG: Cache cleared.");
        } catch (error) {
            console.error("Cache clear error:", error);
        }
    }

    async function getAllAnimals() {
        try {
            const cachedAnimals = await redisClient.get('all_animals');
            if (cachedAnimals) {
                return JSON.parse(cachedAnimals);
            }
        } catch (e) {
            console.error("Redis get error:", e);
        }

        const animals = await knex('animals').select('*');
        
        try {
            await redisClient.setEx('all_animals', 60, JSON.stringify(animals));
        } catch (e) {
            console.error("Redis set error:", e);
        }

        return animals;
    }

    async function getAnimalById(id) {
        return knex('animals').where({ id }).first();
    }

    async function addAnimal(animalData) {
        const [result] = await knex('animals').insert(animalData).returning('id');
        const id = result.id || result; 
        
        await clearCache(); 
        return getAnimalById(id); 
    }

    async function updateAnimal(id, changes) {
        await knex('animals').where({ id }).update(changes);
        
        await clearCache(); 
        return getAnimalById(id);
    }

    async function removeAnimal(id) {
        const result = await knex('animals').where({ id }).del(); 
        
        await clearCache(); 
        return result; 
    }

    console.log("DEBUG: Knex and animalModel initialized."); 

    module.exports = {
        getAllAnimals,
        getAnimalById,
        addAnimal,
        updateAnimal,
        removeAnimal,
    };

} catch (error) {
    console.error("FATAL ERROR IN ANIMAL MODEL INITIALIZATION:", error.message);
    process.exit(1); 
}