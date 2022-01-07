import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Building } from '../../model/building';
import { ActivatedRoute, Router } from '@angular/router';
import { BuildingService } from '../../services/building.service';

@Component({
	selector: 'app-building-form',
	templateUrl: './building-form.component.html',
	styleUrls: ['./building-form.component.scss'],
})
export class BuildingFormComponent implements OnInit {
	building: Building = null;
	form: FormGroup = this.initForm();
  id: string = null;

	constructor(
		private router: Router,
		private route: ActivatedRoute,
		private buildingService: BuildingService,
		private formBuilder: FormBuilder
	) {
	}
	ngOnInit() {
		this.id = this.route.snapshot.paramMap.get('id');

		if (this.id !== null && this.id !== 'new') {
			this.buildingService.get(this.id).subscribe((data) => {
				this.form = this.initForm(data);
				this.building = data;

      });
		} else {
      this.building = new Building();
			this.buildingService.getNewId().subscribe((data) => this.building.id = data);
			this.form = this.initForm(this.building);
		}
		console.log(this.id)
	}

	getButtonFormat(): string {
		return this.id === 'new' ? 'common.button.save' : 'common.button.edit';

	}

	// this.formBuilder.group must be made dynamic

	initForm(building?: Building) {
		return this.formBuilder.group({
			name: new FormControl(
				building?.name || '',
				[Validators.required, Validators.maxLength(50)]
			),
			address: new FormControl(
				building?.address || '',
				[Validators.required, Validators.maxLength(50)]
			),
			index: new FormControl(
				building?.index || '',
				[Validators.required, Validators.pattern('^NO\\w{1,}')]
			),
			sectorCode: new FormControl(
				{
					value: building?.sectorCode || '',
					disabled: this.id !== 'new',
				},
				[Validators.required]
			),
			energyUnitMax: new FormControl(
				{
					value: building?.energyUnitMax || '',
					disabled: this.id !== 'new',
				},
				[Validators.required]
			),
			energyUnits: new FormControl(
				building?.energyUnits || '',
				[Validators.required, Validators.max(building?.energyUnitMax), Validators.min(1)]
			),
		});
	}



	hasError(path: string, errorCode: string) {
		return this.form && this.form.hasError(errorCode, path);
	}

	navigateToBuildingsList() {
		this.router.navigate(['buildings']).then();
	}



	submit() {
		const buildingToSave = { ...this.form.value, id: this.building.id };
		if (this.id !== 'new') {
      this.buildingService.put(buildingToSave).subscribe();
		} else {
			this.buildingService.post(buildingToSave).subscribe();
		}
		this.navigateToBuildingsList();
	}
}
