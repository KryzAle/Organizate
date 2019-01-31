package com.kryzcorp.kryzale.organizate.interfaces;


import com.kryzcorp.kryzale.organizate.fragments.BienvenidaFragment;
import com.kryzcorp.kryzale.organizate.fragments.ConsultarListaEventosFragment;
import com.kryzcorp.kryzale.organizate.fragments.ConsutarListaEventosPorFechaFragment;
import com.kryzcorp.kryzale.organizate.fragments.MiPerfilFragment;
import com.kryzcorp.kryzale.organizate.fragments.RegistrarEventoFragment;

/**
 * Created by CHENAO on 5/08/2017.
 */

public interface IFragments extends BienvenidaFragment.OnFragmentInteractionListener,MiPerfilFragment.OnFragmentInteractionListener,
        RegistrarEventoFragment.OnFragmentInteractionListener,
        ConsultarListaEventosFragment.OnFragmentInteractionListener,ConsutarListaEventosPorFechaFragment.OnFragmentInteractionListener{
}
