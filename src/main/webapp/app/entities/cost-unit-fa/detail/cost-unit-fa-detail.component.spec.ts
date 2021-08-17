import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CostUnitFaDetailComponent } from './cost-unit-fa-detail.component';

describe('Component Tests', () => {
  describe('CostUnitFa Management Detail Component', () => {
    let comp: CostUnitFaDetailComponent;
    let fixture: ComponentFixture<CostUnitFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CostUnitFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ costUnit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CostUnitFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CostUnitFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load costUnit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.costUnit).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
