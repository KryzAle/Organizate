package com.kryzcorp.kryzale.organizate.interfaces;


import com.kryzcorp.kryzale.organizate.fragments.BienvenidaFragment;
import com.kryzcorp.kryzale.organizate.fragments.ConsultaListaUsuarioImagenUrlFragment;
import com.kryzcorp.kryzale.organizate.fragments.ConsultaUsuarioUrlFragment;
import com.kryzcorp.kryzale.organizate.fragments.ConsultarListaUsuariosFragment;
import com.kryzcorp.kryzale.organizate.fragments.ConsultarUsuarioFragment;
import com.kryzcorp.kryzale.organizate.fragments.ConsutarListausuarioImagenFragment;
import com.kryzcorp.kryzale.organizate.fragments.MiPerfilFragment;
import com.kryzcorp.kryzale.organizate.fragments.RegistrarUsuarioFragment;

/**
 * Created by CHENAO on 5/08/2017.
 */

public interface IFragments extends BienvenidaFragment.OnFragmentInteractionListener,MiPerfilFragment.OnFragmentInteractionListener,
        RegistrarUsuarioFragment.OnFragmentInteractionListener,ConsultarUsuarioFragment.OnFragmentInteractionListener,
        ConsultarListaUsuariosFragment.OnFragmentInteractionListener,ConsutarListausuarioImagenFragment.OnFragmentInteractionListener,
        ConsultaUsuarioUrlFragment.OnFragmentInteractionListener,ConsultaListaUsuarioImagenUrlFragment.OnFragmentInteractionListener{
}
