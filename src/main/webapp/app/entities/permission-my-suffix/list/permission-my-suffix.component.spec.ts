jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionMySuffixService } from '../service/permission-my-suffix.service';

import { PermissionMySuffixComponent } from './permission-my-suffix.component';

describe('Component Tests', () => {
  describe('PermissionMySuffix Management Component', () => {
    let comp: PermissionMySuffixComponent;
    let fixture: ComponentFixture<PermissionMySuffixComponent>;
    let service: PermissionMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PermissionMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(PermissionMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PermissionMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PermissionMySuffixService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.permissions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
