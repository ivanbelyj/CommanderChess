package ivan_belyj.commanderchess.model.availability;

public enum AvailabilityState {
    UNKNOWN,
    FREE,
    BLOCKED,
    SELF,  // Сам элемент, для которого определяется доступность
    DESTROY  // Вражеская фигура доступна для уничтожения
}
