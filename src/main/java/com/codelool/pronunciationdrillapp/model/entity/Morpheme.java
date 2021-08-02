package com.codelool.pronunciationdrillapp.model.entity;

import java.util.List;

public abstract class Morpheme {

	public abstract String getAlphabetic();

	public abstract String getPhonetic();

	public abstract List<Word> getWords();

}
