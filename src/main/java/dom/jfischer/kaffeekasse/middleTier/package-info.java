/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
/**
 * middle tier.
 *
 * <h1>items interesting for programming frontends</h1>
 *
 * use interface {@link dom.jfischer.kaffeekasse.middleTier.Command} for
 * communication between any frontend and the middle tier. the class
 * {@link dom.jfischer.kaffeekasse.middleTier.CommandImpl} contains the core of
 * the middle tier. This class requires an implementation of interface
 * {@link dom.jfischer.kaffeekasse.middleTier.Slave} so that for each frontend
 * there must be an implementation of the @link
 * dom.jfischer.kaffeekasse.middleTier.ISlave} interface.
 *
 * Moreover, frontend should use interface
 * {@link dom.jfischer.kaffeekasse.middleTier.TallyPDF} for storing tallies. the
 * class {@link dom.jfischer.kaffeekasse.middleTier.TallyPDFImpl} contains its
 * implementation.
 */
package dom.jfischer.kaffeekasse.middleTier;
