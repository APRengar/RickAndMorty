package example.rickandmortyapp.data.local

import example.rickandmortyapp.RickMortyCharacter
import example.rickandmortyapp.data.CharacterEntity

fun RickMortyCharacter.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image
    )
}

fun CharacterEntity.toDomain(): RickMortyCharacter {
    return RickMortyCharacter(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image
    )
}