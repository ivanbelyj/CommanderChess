package ivan_belyj.commanderchess.model;

/** В некоторых случаях (например, отрисовка) требуется знать не только параметры, но и тип фигуры **/
public enum FigureType {
    Commander,
    Infantry,
    Tank,
    Militia,
    Engineer,
    Artillery,
    AntiAircraftGun,
    Missile,
    AirForce,
    Navy,
    Headquarters
}
