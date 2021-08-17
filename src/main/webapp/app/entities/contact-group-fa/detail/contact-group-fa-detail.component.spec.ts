import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactGroupFaDetailComponent } from './contact-group-fa-detail.component';

describe('Component Tests', () => {
  describe('ContactGroupFa Management Detail Component', () => {
    let comp: ContactGroupFaDetailComponent;
    let fixture: ComponentFixture<ContactGroupFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactGroupFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactGroup: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactGroupFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactGroupFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactGroup).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
