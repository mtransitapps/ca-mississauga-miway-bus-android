package org.mtransit.parser.ca_mississauga_miway_bus;

import static org.mtransit.commons.StringUtils.EMPTY;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MTrip;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

// http://www.mississauga.ca/portal/miway/developerdownload
public class MississaugaMiWayBusAgencyTools extends DefaultAgencyTools {

	public static void main(@NotNull String[] args) {
		new MississaugaMiWayBusAgencyTools().start(args);
	}

	@Nullable
	@Override
	public List<Locale> getSupportedLanguages() {
		return LANG_EN;
	}

	@Override
	public boolean defaultExcludeEnabled() {
		return true;
	}

	@NotNull
	@Override
	public String getAgencyName() {
		return "Mississauga MiWay";
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public boolean defaultAgencyColorEnabled() {
		return true;
	}

	@Override
	public boolean defaultRouteIdEnabled() {
		return true;
	}

	@Override
	public boolean useRouteShortNameForRouteId() {
		return false; // used for GTFS-RT
	}

	@Override
	public long getRouteId(@NotNull GRoute gRoute) {
		return super.getRouteId(gRoute); // used for GTFS-RT
	}

	@Override
	public boolean defaultRouteLongNameEnabled() {
		return true;
	}

	@Override
	public boolean directionFinderEnabled() {
		return true;
	}

	@NotNull
	@Override
	public List<Integer> getDirectionTypes() {
		return Arrays.asList(
				MTrip.HEADSIGN_TYPE_DIRECTION,
				MTrip.HEADSIGN_TYPE_STRING
		);
	}

	@NotNull
	@Override
	public String cleanDirectionHeadsign(int directionId, boolean fromStopName, @NotNull String directionHeadSign) {
		directionHeadSign = cleanHeadSign(directionHeadSign);
		return directionHeadSign; // keep original head-sign bounds for convert to direction E/W/N/S
	}

	private static final Pattern REMOVE_BOUNDS_ = CleanUtils.cleanWords("eastbound", "westbound", "northbound", "southbound");

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = REMOVE_BOUNDS_.matcher(tripHeadsign).replaceAll(EMPTY); // bounds used for direction
		return cleanHeadSign(tripHeadsign);
	}

	@NotNull
	private String cleanHeadSign(@NotNull String headSign) {
		headSign = CleanUtils.cleanStreetTypes(headSign);
		headSign = CleanUtils.cleanNumbers(headSign);
		return CleanUtils.cleanLabel(headSign);
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.cleanBounds(gStopName);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		gStopName = CleanUtils.cleanNumbers(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	@Override
	public int getStopId(@NotNull GStop gStop) {
		return super.getStopId(gStop); // used for GTFS-RT
	}
}
